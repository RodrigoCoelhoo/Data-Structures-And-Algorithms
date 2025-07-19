import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		boolean running = true;
		String[] algorithms = {"Bubble Sort"};


		SortAlgorithm<Integer> algorithm = null;

		while(running)
		{
			ArrayList<Integer> list = new ArrayList<Integer>();

			// Select Algorithm
			while(true) {
				printAlgorithms(algorithms);
				String algorithmChoice = sc.nextLine();
				switch (algorithmChoice) {
					case "1":
						algorithm = new BubbleSort<Integer>();
						break;
					case "Exit":
						running = false;
						System.exit(0);
					default:
						System.out.println("Invalid option! Try again.");
						continue;
				}
				break;
			}

			algorithm.info();

			while (true) {
				System.out.println("Choose the number of elements [10, 200]");
				
				int nElements;
				while(true)
				{	
					nElements = sc.nextInt();
					if(nElements >= 10 && nElements <= 200) break;

					System.out.println("The selected number is outside the valid range! Try again");
				}

				Random r = new Random();
				for(int i = 0; i < nElements; i++)
				{
					list.add(r.nextInt(1, 100));
				}
				
				break;
			}

			System.out.println("");
			
			printList(list);

			algorithm.sort(list);

			printList(list);

			System.out.println();
		}

		sc.close();
	}

	public static void printAlgorithms(String[] algorithms) {
		System.out.println("== Algorithms ==");
		for(int i = 0; i < algorithms.length ; i++)
		{
			System.out.println(i+1 + ". " + algorithms[i]);
		}
		System.out.println("");

		System.out.println("Type 'Exit' to end the program!");
		System.out.println("================");
	}

	public static void printList(ArrayList<Integer> list)
	{
		System.out.print("[");

		for(int i = 0; i < list.size(); i++)
		{
			System.out.print(list.get(i));
			if(i != list.size() - 1)
				System.out.print(", ");
		}
		System.out.println("]");
	}
}
