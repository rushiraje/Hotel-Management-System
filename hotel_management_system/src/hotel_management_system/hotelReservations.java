package hotel_management_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class hotelReservations {
	private static final String url="jdbc:mysql://localhost:3306/hotel_db";
	private static final String username="root";
	private static final String password="1234";
	
	public static void main(String[] args) throws ClassNotFoundException,SQLException, InterruptedException{
		try{
		Class.forName("jdbc:mysql://localhost:3306/hotel_db\",\"root\",\"1234\"");
		}
		catch(ClassNotFoundException e ) {
			System.out.println(e.getMessage()); 
		}
		
		try {
		Connection connection =DriverManager.getConnection(url,username,password);
		while(true) {
			System.out.println("------------************----------------");
		System.out.println("HOTEL RESERVATION SYSTEM");
		Scanner scanner= new Scanner(System.in);
		
		System.out.println("1. Reserve a room ");
		System.out.println("2. Show room Reservations ");
		System.out.println("3. Get a Room Number");
		System.out.println("4. Update Reservation");
		System.out.println("5. Delete Reservation");
		System.out.println("0. EXITE");
		System.out.println("CHOOSE THE OPERATON : ");
		int choice=scanner.nextInt();
		
		switch(choice) {
		case 1: 
			reserveRoom(connection , scanner);
			break;
		case 2 : displayReservation(connection);
		    break;
		case 3 : GetRoomnumber(connection,scanner);
		    break;
		case 4 : UpdateReservation(connection,scanner);
		    break;
		case 5 : DeleteReservation(connection,scanner);
		    break;
		case 0: exit();
		       scanner.close();
		       return;
		       default: System.out.println("invalid choice try some valid input");
		}
		}
		}		
      catch(SQLException e) {
	System.out.println(e.getMessage());
}
}
	private static void exit() throws InterruptedException {
		System.out.print("Exiting System");
        int i = 5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(1000);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou For Using Hotel Reservation System!!!");
    }
	private static void DeleteReservation(Connection connection, Scanner scanner) {
		 try {
	            System.out.print("Enter reservation ID to delete: ");
	            int reservationId = scanner.nextInt();

	            if (!reservationExists(connection, reservationId)) {
	                System.out.println("Reservation not found for the given ID.");
	                return;
	            }

	            String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;

	            try (Statement statement = connection.createStatement()) {
	                int affectedRows = statement.executeUpdate(sql);

	                if (affectedRows > 0) {
	                    System.out.println("Reservation deleted successfully!");
	                } else {
	                    System.out.println("Reservation deletion failed.");
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
    private static boolean reservationExists(Connection connection, int reservationId) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next(); // If there's a result, the reservation exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }
	private static void UpdateReservation(Connection connection, Scanner scanner) {
		try {
            System.out.print("Enter reservation ID to update: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            System.out.print("Enter new guest name: ");
            String newGuestName = scanner.nextLine();
            System.out.print("Enter new room number: ");
            int newRoomNumber = scanner.nextInt();
            System.out.print("Enter new contact number: ");
            String newContactNumber = scanner.next();

            String sql = "UPDATE reservations SET guest_name = '" + newGuestName + "', " +
                    "room_number = " + newRoomNumber + ", " +
                    "contact_number = '" + newContactNumber + "' " +
                    "WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation updated successfully!");
                } else {
                    System.out.println("Reservation update failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}
	private static void GetRoomnumber(Connection connection, Scanner scanner) {
		try{
			System.out.println("enter a room number : ");
			int reservationid=scanner.nextInt();
			System.out.println("enter a guest name : ");
			String guestname=scanner.next();
			
			String sql = "SELECT room_number FROM reservations " +
                    "WHERE reservation_id = " + reservationid +
                    " AND guest_name = '" + guestname + "'";
			
			try (Statement statement = connection.createStatement();
	                 ResultSet resultSet = statement.executeQuery(sql)) {
				if (resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_number");
                    System.out.println("Room number for Reservation ID " + reservationid +
                            " and Guest " + guestname + " is: " + roomNumber);
                } else {
                    System.out.println("Reservation not found for the given ID and guest name.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
			}
	}

	private static void displayReservation(Connection connection)throws SQLException {
		String sql= "SELECT reservation_id,guest_name,room_number,contact_number,reservation_date FROM Reservations";
		try(Statement statement = connection.createStatement();
	             ResultSet resultSet = statement.executeQuery(sql)){
			
			System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date        |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            
            while(resultSet.next()){
            	int reservationid=resultSet.getInt("reservation_id");
            	String guestname=resultSet.getString("guest_name");
            	int roomnumber=resultSet.getInt("room_number");
            	String contactnumber=resultSet.getString("contact_number");
            	String reservationDate = resultSet.getTimestamp("reservation_date").toString();
            	
            	System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        reservationid, guestname, roomnumber, contactnumber, reservationDate);
            }
		}
	}
private static void reserveRoom(Connection connection, Scanner scanner) {
		System.out.println("Enter a Guest Name");
		String Guestname=scanner.next();
		scanner.nextLine();
		System.out.println("Enter a Room Number");
		int roomnumber=scanner.nextInt();
		System.out.println("Enter a Contact Number");
		String Contactnumber=scanner.next();
		
		String sql = "INSERT INTO Reservations(guest_name, room_number, contact_number) " +
	             "VALUES('" + Guestname + "', '" + roomnumber + "', '" + Contactnumber + "')";
		try
		(Statement statement= connection.createStatement()){
				
					int affectedrows= statement.executeUpdate(sql);//excuteupdate is used when we have to perform insert , update, delete query on data 
					if(affectedrows>0) {
						System.out.println("reservation is succesfull");
					}
					else {
						System.out.println("reservation is failed");
					}	
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
	}
}