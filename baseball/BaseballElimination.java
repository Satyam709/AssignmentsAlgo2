import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;

public class BaseballElimination {
    // create a baseball division from given filename in format specified below
    private final int totalTeams;

    private final HashMap<String, Integer> teams;
    private final int[] wins;
    private final int[] loses;
    private final int[] rem;

    private final int[][] schedule;

    public BaseballElimination(String filename) {
        if (filename == null) throw new IllegalArgumentException("Invalid file name");

        In in = new In(filename);
        totalTeams = Integer.parseInt(in.readLine());
        teams = new HashMap<>(totalTeams);
        wins = new int[totalTeams];
        loses = new int[totalTeams];
        rem = new int[totalTeams];
        schedule = new int[totalTeams][totalTeams];
        System.out.println("printing:");

        for (int i = 0; i < totalTeams; i++) {
            String[] cl = in.readLine().split("\\s+");
            teams.put(cl[0], i);
            wins[i] = Integer.parseInt(cl[1]);
            loses[i] = Integer.parseInt(cl[2]);
            rem[i] = Integer.parseInt(cl[3]);
            for (int j = 0; j < totalTeams; j++)
                schedule[i][j] = Integer.parseInt(cl[4 + j]);
        }

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

    public boolean isEliminated(String team)              // is given team eliminated?
    {
        return false;
    }

    public Iterable<String> certificateOfElimination(
            String team)  // subset R of teams that eliminates given team; null if not eliminated
    {
        return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
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
    }
}
