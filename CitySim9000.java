//Maria Levandoski
//CS1632 Deliverable 2

import java.util.Random;

public class CitySim9000 {
    CitySim9000 car;
    Node source;
    Node destination;
    String street;

    //Constructor
    public CitySim9000(Node s, Node d, String st){
        source = s;
        destination = d;
        street = st;
    }

	public static void main(String[] args) {
        CitySim9000 citySim = new CitySim9000(null,null,null);
        citySim.setup(args);
	}



    public void setup(String[] args) {
        /* Sets up the city.
         * Four unique city locations.
         * (Outside City has four nodes that all stand in for the same location.)
         */
        Node outsideTopLeft = new Node("Outside City");
        Node outsideTopRight = new Node("Outside City");
        Node outsideBottomLeft = new Node("Outside City");
        Node outsideBottomRight = new Node("Outside City");
        Node mall = new Node("Mall");
        Node bookstore = new Node("Bookstore");
        Node coffee = new Node("Coffee");
        Node uni = new Node("University");

        /* Connects the nodes to one another and sets the streets between them.
         * 4 streets are present.
         */
        outsideTopLeft.setEast(mall, "Fourth Ave.");
        mall.setEast(bookstore, "Fourth Ave.");
        mall.setSouth(coffee, "Meow St.");
        bookstore.setEast(outsideTopRight, "Fourth Ave.");
        bookstore.setSouth(uni, "Chirp St.");
        outsideBottomRight.setWest(uni, "Fifth Ave.");
        uni.setNorth(bookstore, "Chirp St.");
        uni.setWest(coffee, "Fifth Ave.");
        coffee.setNorth(mall, "Meow St.");
        coffee.setWest(outsideBottomLeft, "Fifth Ave.");

        Node[] locations = {null, mall, bookstore, coffee, uni, outsideTopLeft};

        //ensures that there is one command line argument, an integer, or prints an error to the user and exits the program.
        if (args.length == 0 || args.length > 1){
            System.out.printf("CitySim9000 requires one integer for a command line argument.\n");
            System.exit(0);
        }
        int randomSeed = 999;
        try {
            randomSeed = Integer.parseInt(args[0]);
        } catch (NumberFormatException e){
            System.out.printf("CitySim9000 requires one integer for a command line argument.\n");
            System.exit(0);
        }

        Random generator = new Random(randomSeed);
        simulator(locations, generator);
    }

    //The main program. Loops through 5 times, once for each driver.
    public void simulator(Node[] locations, Random generator){
        boolean city;

        for (int driver = 0; driver < 5; driver++) {
            car = new CitySim9000(null, null, null);
            city = true;
            int startingLocation = generator.nextInt(Integer.SIZE - 1);
            city = determineStartingLocation(startingLocation, locations, generator);
            System.out.printf("Driver %d heading from %s to %s via %s\n", driver, car.source.location, car.destination.location, car.street);

            while (city) {
                car.source = car.destination;
                city = determineLocation(generator);

                System.out.printf("Driver %d heading from %s to %s via %s\n", driver, car.source.location, car.destination.location, car.street);
                if (!city){
                    System.out.printf("Driver %d has left the city!\n", driver);
                    System.out.printf("-----\n");
                }
            } // continue until destination is Outside City
        } // iterates through 5 drivers

    }

    //This method is used to determine the starting location, since it is slightly more complicated than subsequent locations.
    public boolean determineStartingLocation(int startingLocation, Node[] locations, Random generator){
        startingLocation = startingLocation % 5;

        if (startingLocation == 0) { //corresponds to "Outside City", pseudorandomly choose University via Fifth or Mall via Fourth
            int rand = generator.nextInt(Integer.SIZE - 1);
            rand = rand % 2;
            car.source = locations[5];
            if (rand == 0){
                car.source.location = "Outside City";
                car.destination = locations[4];
                car.destination.location = "University";
                car.street = "Fifth Ave.";
            } else {
                car.source.location = "Outside City";
                car.destination = locations[1];
                car.destination.location = "Mall";
                car.street = "Fourth Ave.";
            }
        } else {
            car.source = locations[startingLocation];
            boolean bool = determineLocation(generator);
            return bool;
        }
        return true;

    }

    //This method determines subsequent locations..
    public boolean determineLocation(Random generator){
        int rand = generator.nextInt(Integer.SIZE - 1);
        rand = rand % 2;

        int dest = car.source.getDirection(rand);
        if (dest == 0){
            car.destination = car.source.getNorth();
            car.street = car.source.streetNorth;
        } else if (dest == 1){
            car.destination = car.source.getSouth();
            car.street = car.source.streetSouth;
        } else if (dest == 2){
            car.destination = car.source.getEast();
            car.street = car.source.streetEast;
        } else if (dest == 3){
            car.destination = car.source.getWest();
            car.street = car.source.streetWest;
        }


        if ((car.destination.location.equals("Outside City")) || (car.destination.location.equals("Outside City"))){
            return false;
        }
        return true;
    }

    //The node class, representing locations
	public class Node {

		Node north;
		Node south;
		Node east;
		Node west;

		boolean car;

		String location;
		String streetNorth;
		String streetSouth;
		String streetEast;
		String streetWest;

        boolean[] adj = {false, false, false, false};

		private Node(String name) {

			north = null;
			south = null;
			east = null;
			west = null;

            location = name;
		}

		public void setNorth(Node n, String st) {
			north = n;
			streetNorth = st;
            adj[0] = true;
		}
		public void setSouth(Node s, String st) {
			south = s;
			streetSouth = st;
            adj[1] = true;
		}
		public void setEast(Node e, String st) {
			east = e;
			streetEast = st;
            adj[2] = true;
		}
		public void setWest(Node w, String st) {
			west = w;
			streetWest = st;
            adj[3] = true;
		}

		public Node getNorth() {
			return north;
		}
		public Node getSouth() {
			return south;
		}
		public Node getEast() {
			return east;
		}
		public Node getWest() {
			return west;
		}

        // Determines which direction to travel to
        public int getDirection(int here) {
            int check = 0;

            for(int i = 0; i < 4; i++) {
                if(adj[i]) {
                    if(check == here) {
                        return i;
                    } else {
                        check++;
                    }
                }
            }
            return 6;
        }
	}
}
