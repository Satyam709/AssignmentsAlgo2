import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BaseballElimination {
    // create a baseball division from given filename in format specified below
    private final int totalTeams;

    private final HashMap<String, Integer> teams;
    private final int[] wins;
    private final int[] loses;
    private final int[] rem;
    // private final int totalRem;
    private final int[][] schedule;
    private Iterable<String> subset;
    private final String[] intToTeam;

    public BaseballElimination(String filename) {
        if (filename == null) throw new IllegalArgumentException("Invalid file name");

        In in = new In(filename);
        String tmp = in.readLine();
        if (tmp == null)
            throw new IllegalArgumentException();
        totalTeams = Integer.parseInt(tmp);
        teams = new HashMap<>(totalTeams);
        intToTeam = new String[totalTeams];
        wins = new int[totalTeams];
        loses = new int[totalTeams];
        rem = new int[totalTeams];
        schedule = new int[totalTeams][totalTeams];
        subset = null;

        // System.out.println("printing:");

        for (int i = 0; i < totalTeams; i++) {
            tmp = in.readLine();
            if (tmp == null)
                throw new IllegalArgumentException();
            String[] cl = tmp.trim().split("\\s+");
            // System.out.println(Arrays.toString(cl));
            teams.put(cl[0], i);
            intToTeam[i] = cl[0];
            wins[i] = Integer.parseInt(cl[1]);
            loses[i] = Integer.parseInt(cl[2]);
            rem[i] = Integer.parseInt(cl[3]);
            for (int j = 0; j < totalTeams; j++)
                schedule[i][j] = Integer.parseInt(cl[4 + j]);
        }
        // this.totalRem = totalRem;
        System.out.println("wins=" + Arrays.toString(wins));
        System.out.println("lose=" + Arrays.toString(loses));
        System.out.println("rem=" + Arrays.toString(rem));
        System.out.println("map=" + teams);
        System.out.println("schedule:");
        for (int[] i : schedule) {
            for (int j : i)
                System.out.print(j + " ");
            System.out.println();
        }
    }

    public int numberOfTeams()                        // number of teams
    {
        return totalTeams;
    }

    public Iterable<String> teams()                                // all teams
    {
        return teams.keySet();
    }

    public int wins(String team)                      // number of wins for given team
    {
        if (teams.get(team) == null)
            throw new IllegalArgumentException("team " + team + " not found");
        return wins[teams.get(team)];
    }

    public int losses(String team)                    // number of losses for given team
    {
        if (teams.get(team) == null)
            throw new IllegalArgumentException("team " + team + " not found");
        return loses[teams.get(team)];
    }

    public int remaining(String team)                 // number of remaining games for given team
    {
        if (teams.get(team) == null)
            throw new IllegalArgumentException("team " + team + " not found");
        return rem[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (teams.get(team1) == null || teams.get(team2) == null)
            throw new IllegalArgumentException("any of the teams not found");

        return schedule[teams.get(team1)][teams.get(team2)];
    }

    private boolean compute(String team) {
        int totalV = totalTeams + 1 + (totalTeams - 1) * (totalTeams - 2)
                / 2; // +1 for source because one extra for tail is counted in totalTeams
        System.out.println("total vetices = " + totalV);
        if (teams.get(team) == null)
            throw new IllegalArgumentException("team " + team + " not found");
        int queryTeam = teams.get(team);
        FlowNetwork net = new FlowNetwork(totalV);
        int s = 0, t = totalV - 1, totalRem = 0;
        int count = totalTeams - 1;
        for (int i = 0, k = 0; i < totalTeams; i++) {
            if (i == queryTeam) continue;
            k++; // indicating team no
            int capacity = rem[queryTeam] + wins[queryTeam] - wins[i];
            if (capacity < 0) {
                ArrayList<String> teamSet = new ArrayList<>(1);
                teamSet.add(intToTeam[i]);
                this.subset = teamSet;
                return true;
            }
            net.addEdge(
                    new FlowEdge(k, t, capacity)); // indexing vertex like 1,2,3..
            for (int j = 0; j < totalTeams; j++) {
                if (j > i) {
                    if (j != queryTeam) {
                        count++; // current combo no.
                        totalRem += schedule[i][j];
                        net.addEdge(new FlowEdge(s, count,
                                                 schedule[i][j])); // storing s->combination of ij+1 avoid conflict with vertex entry as unique 1d notation
                        net.addEdge(new FlowEdge(count, k,
                                                 Integer.MAX_VALUE)); // these 2 lines add the combinations to their respective teams no with infinite cap
                        net.addEdge(
                                new FlowEdge(count, j < queryTeam ? j + 1 : j, Integer.MAX_VALUE));
                    }
                }
            }
        }
        System.out.println(count);
        System.out.println(net);
        FordFulkerson algo = new FordFulkerson(net, s, t);
        System.out.println("algoValue = " + algo.value());
        System.out.println("total rem = " + totalRem);
        boolean isEle = algo.value() != totalRem; // if all matches rem flowed through
        if (isEle) {
            ArrayList<String> teamSet = new ArrayList<>(totalTeams);
            for (int i = 0; i < totalTeams; i++) {
                if (i == queryTeam) continue;
                String teamm = intToTeam[i];
                if (algo.inCut(i < queryTeam ? i + 1 : i))
                    teamSet.add(teamm);
            }
            this.subset = teamSet;
        }
        else this.subset = null;
        return isEle;
    }

    public boolean isEliminated(String team)              // is given team eliminated?
    {
        return compute(team);
    }

    public Iterable<String> certificateOfElimination(
            String team)  // subset R of teams that eliminates given team; null if not eliminated
    {
        if (teams.get(team) == null)
            throw new IllegalArgumentException("team " + team + " not found");
        compute(team);
        Iterable<String> temp = subset;
        subset = null;
        return temp;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        List<String> ele = new LinkedList<>();
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                ele.add(team);
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
        System.out.println("Elementaed teams are :" + ele);
    }
}