import java.util.*;

class Farkle {
    private static int diceSides = 6;
    private static int[] wilds = {1,5};

    static ArrayList<Integer> throwDice(int n) {
        ArrayList<Integer> results = new ArrayList<Integer>();
        Random rand = new Random();
        for(int i=0; i<n; i++) {
            results.add(rand.nextInt(diceSides) + 1);
        }
        return results;
    }

    static float getResults(ArrayList<ArrayList<Integer>> casts) {
        int points = 0;
        for (ArrayList<Integer> c : casts) {
            for(int i : wilds) {
                points += Collections.frequency(c, i);
            }
        }
        return (float)points/casts.size();
    }
    

    static void printResults(ArrayList<ArrayList<Integer>> casts) {
        ArrayList<Integer> dice = new ArrayList<Integer>();
        casts.forEach(dice::addAll);
        int freq;
        float perc;
        for(int i=1; i<=diceSides; i++) {
            freq = Collections.frequency(dice, i);
            perc = (float)freq / dice.size() * 100;
            System.out.println(i + ": " + freq + " (" + perc + "%)");
        }
    }

    static void gotAWild(ArrayList<ArrayList<Integer>> casts) {
        // find how many casts got 0, 1, ... points
        HashMap<Integer, MutableInt> pointsPerCast = new HashMap<Integer, MutableInt>();
        int points = 0;
        MutableInt p;
        for (ArrayList<Integer> c : casts) {
            points = 0;
            for(int i : wilds) {
                points += Collections.frequency(c, i);
            }
            p = pointsPerCast.get(points);
            if(p==null)
                pointsPerCast.put(points, new MutableInt());
            else
                p.increment();
        }

        // print the results
        System.out.print("\nNumber of times a cast resulted in points.\n-------\n");
        int greaterThanZero = 0;
        for(int k : pointsPerCast.keySet()) {
            MutableInt v = pointsPerCast.get(k);
            System.out.println(k + " points: " + v.get() + " times (" + (float)v.get()/casts.size()*100 + "%)");
            if(k > 0)
                greaterThanZero += v.get();
        }
        System.out.println(">0 points: " + greaterThanZero + " times (" + (float)greaterThanZero/casts.size()*100 + "%)");            
    }

    static void experiment(int numDice, int numTries) {
        ArrayList<ArrayList<Integer>> casts = new ArrayList<ArrayList<Integer>>();
        for( int i=0; i< numTries; i++ )
            casts.add(throwDice(numDice));
        System.out.println("\nFor " + numDice + " dice, the average number of points per cast is:\n" + getResults(casts));
        System.out.print("\nFull results (# on die: how many times that # was rolled)\n-------\n");
        printResults(casts);
        System.out.println("Total casts: " + numTries);
        gotAWild(casts);
    }

    public static void main(String[] args) {
        int numTries = 100000;

        System.out.println("1 point is awarded for each " + printWilds() + " thrown.\n--------");

        //try with a single dice
        int numDice = 1;
        experiment(numDice, numTries);

        //try with 3 dice
        numDice = 3;
        experiment(numDice, numTries);
    }

    static String printWilds() {
        String r = "";
        for(int i=0; i<wilds.length; i++)
            if(i == wilds.length-1)
                r += wilds[i];
            else
                r += wilds[i] + " or ";
        return r;
    }
}


class MutableInt {
    int value = 1;

    public void increment() { 
        ++ value; 
    }
    
    public int get() {
        return value;
    }
}