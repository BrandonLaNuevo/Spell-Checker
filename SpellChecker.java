import java.io.*;
import java.util.*;

class SpellChecker
{
    // Official Dictionary (84,000+ words) variable
    private static String dictionary = "dictionary.txt";
    // User Dictionary variable
    private static String userDictionary = "user_dictionary.txt";
    // Hashmap for the dictionary
    private static HashMap<String, String> dictionaryHashMap = new HashMap<String, String>();
    // ArrayList  for the user inputted dictionary
    private static ArrayList<String> dictionaryArrayList = new ArrayList<String>();
    private static String finalChoice;

    public static void main(String args[])
    {
        SpellChecker spcheck = new SpellChecker();
        try
        {
            spcheck.menu();
            spcheck.generateArrayList(finalChoice);
            Collections.sort(spcheck.dictionaryArrayList);
            spcheck.generateHashMap(finalChoice);
            spcheck.spellCheckPrompt();
        }
        catch(Exception e)  // Lazy error check
        {
            System.out.println("I/O Error.");
        }

    }

    // Entire menu involving user input before spell Check utility
    public void menu() throws IOException
    {
        Scanner userinput = new Scanner(System.in);
        SpellChecker spcheck = new SpellChecker();

        System.out.println("--------------------------");
        System.out.println("Spell Checker Application");
        System.out.println("--------------------------");
        System.out.println("1. Use a user defined Dictionary");
        System.out.println("2. Use a a already defined Dictionary");
        System.out.println("--------------------------");
        System.out.print("Entry: ");
        while(!userinput.hasNextInt())
        {
            userinput.next();
            System.out.println("Invalid input, Enter an Integer:");
            System.out.println("1. Use a user defined Dictionary");
            System.out.println("2. Use a a already defined Dictionary");
            System.out.println("--------------------------");
            System.out.print("Entry: ");
        }
        int choice = userinput.nextInt();
        while(choice < 3)
        {
            switch(choice)
            {
                case 1:
                    spcheck.entryPrompt();
                    break;
                case 2:
                    spcheck.dictionaryPrompt();
                    break;
            }
            break;
        }
        while(choice > 2)
        {
            System.out.println("Invalid input, Enter 1 or 2");
            System.out.println("1. Use a user defined Dictionary");
            System.out.println("2. Use an  already defined Dictionary");
            System.out.println("--------------------------");
            System.out.print("Entry: ");
            choice = userinput.nextInt();
        }
        // sets finalChoice according to what the user chooses
        // finalChoice used to specify what dictionary to use for later methods
        if(choice == 1)
        {
            finalChoice = userDictionary;
        }
        if(choice == 2)
        {
            finalChoice = dictionary;
        }
    }

    // Prompt for when choosing to use offical dictionary
    public void dictionaryPrompt()
    {

        System.out.println("Loading dictionary \"" + dictionary + "\"");
    }

    // Prompt for when choosing to use user defined dictionary
    public void entryPrompt()
    {
        System.out.println("Loading dictionary \"" + userDictionary + "\"");
        System.out.println("Please enter Strings that you would like to add to \"" + userDictionary + "\"");
        System.out.println("When finished type in \"DONE\". ");
        Scanner userinput = new Scanner(System.in);
        try
        {
            FileWriter fwrite = new FileWriter(userDictionary);
            BufferedWriter writer = new BufferedWriter(fwrite);
            // Prompts user for input to add to userDictionary
            while(true)
            {
                System.out.print("Entry: ");
                String temp = userinput.next();
                if (temp.equals("DONE"))
                {
                    System.out.println("Exiting.");
                    break;
                    // print out all the added dictionary entries
                }
                writer.write(temp);
                writer.newLine();
                // Reaffirms user what they entered was added
                System.out.println("Your string " + temp + " was added to the dictionary." );
            }
            writer.close();
        }
        catch(Exception e)  // Lazy error check
        {
            System.out.println("File Related error.");
        }
        System.out.println("Done Adding to the Dictionary.");
        System.out.println("You added: ");
        // Lists what words / Strings the user inputted
        System.out.println("--------------------------");
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(userDictionary));
            String fileLine;
            while((fileLine = reader.readLine()) != null)
            {
                System.out.print(fileLine + "  ");
            }
            System.out.print("\n");
            System.out.println("--------------------------");
        }
        catch(Exception e)  // Lazy error check
        {
            System.out.println("File releated error.");
        }
    }

    public void spellCheckPrompt()
    {
        Scanner userinput = new Scanner(System.in);
        System.out.println("Please enter a string that you want to be spell checked");
        System.out.println("When finished type in \"DONE\". ");
        while(true)
        {
            System.out.print("Entry: ");
            String userString = userinput.next();
            if(userString.equals("DONE"))
            {
                break;
                // print out all the added dictionary entries
            }
            ArrayList<String> predictions = changes(userString);
            HashMap<String, String> prediction  = new HashMap<String, String>();
            // Checks if direct match, prints Correct
            if(dictionaryHashMap.containsKey(userString))
            {
                System.out.println("Correct");
            }
            // Checks if there does not exist a match, then prints no matches
            if(!dictionaryHashMap.containsKey(userString))
            {
                System.out.println("No matches");
            }
            int numOfSuggestions = 0;
            // Checks for slight changes to spelling with changes ArrayList
            for(String s : predictions)
            {
                if(dictionaryHashMap.containsKey(s))
                {
                    numOfSuggestions++;
                    break;
                }
            }
            // Lazy way to keep number of matches
            // Would of normally figured out a way to have seperate statements
            // for one correct match vs. possible matches.
            if(numOfSuggestions <= 1)
            {
                System.out.println(numOfSuggestions + " match / possible matches.");
            }
        }
    }

    public ArrayList<String> changes(String word)
    {
        ArrayList<String> result = new ArrayList<String>();

        // All deletes of a single letter
        for(int i=0; i < word.length(); ++i)
        {
            result.add(word.substring(0, i) + word.substring(i+1));
        }

        // All swaps of adjacent letters
        for(int i=0; i < word.length()-1; ++i)
        {
            result.add(word.substring(0, i) + word.substring(i+1, i+2) +
                    word.substring(i, i+1) + word.substring(i+2));
        }

        // All replacements of a letter
        for(int i=0; i < word.length(); ++i)
        {
            for(char c='a'; c <= 'z'; ++c)
            {
                result.add(word.substring(0, i) + String.valueOf(c) +
                        word.substring(i+1));
            }
        }

        // All insertions of a letter
        for(int i=0; i <= word.length(); ++i)
        {
            for(char c='a'; c <= 'z'; ++c)
            {
                result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
            }
        }
        return result;
    }

    // Fills arrayList with strings from desired dictionary
    public void generateArrayList(String file) throws IOException
    {
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while((line = reader.readLine()) != null)
        {
            dictionaryArrayList.add(line);
        }
    }

    // Fills hashmap with strings from desired dictionary
    public void generateHashMap(String file) throws IOException
    {
        for(int i = 0; i < dictionaryArrayList.size(); i++)
        {
            dictionaryHashMap.put(dictionaryArrayList.get(i), dictionaryArrayList.get(i));
        }
    }
}
