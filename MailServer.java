import java.io.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * MailServer - Acts as a mail server
 * Name: Luke Doherty Tim Lonergan Andrew Susz
 * Course/Section: ISTE-121-02
 * Final Group Project
 * Date: 12/6/2021
 */

public class MailServer extends Application implements EventHandler<ActionEvent> {
   // Window Attributes
   private Stage stage;
   private Scene scene;
   private VBox root = null;

   // GUI components
   private TextArea taList = new TextArea();
   
   // Networking
   private static final int SERVER_PORT = 35353; 
   private long startTime = 0;
   private Vector<String> users = new Vector<String>();  
   
   // Important Instantiations
   private Email[] bobBox = new Email[1];
   private Email[] johnBox = new Email[1];
   private Email[] lucyBox = new Email[1];
   private int globalUser = 0;
   private int userTarget = 0;
    
   /** Main program */
   public static void main(String[] args) {
      launch(args);
   }
   
   /** start the server */
   public void start(Stage _stage) {
      stage = _stage;
      stage.setTitle("Mail Server - Testing GUI");
      final int WIDTH = 450;
      final int HEIGHT = 400;
      final int X = 550;
      final int Y = 100;
   
      stage.setX(X);
      stage.setY(Y);
      stage.setOnCloseRequest(
         new EventHandler<WindowEvent>() {
            public void handle(WindowEvent evt) { System.exit(0); }
         });
      
      // Set up root
      root = new VBox();
      
      // Put clear button in North
      HBox hbNorth = new HBox();
      hbNorth.setAlignment(Pos.CENTER);
      
      // Set up rootis
      root.getChildren().addAll(hbNorth, taList);
      for(Node n: root.getChildren()) {
         VBox.setMargin(n, new Insets(10));
      }
      
      // Set the scene and show the stage
      scene = new Scene(root, WIDTH, HEIGHT);
      stage.setScene(scene);
      stage.show();
      
      // Adjust size of TextArea
      taList.setPrefHeight(HEIGHT - hbNorth.getPrefHeight());
      
      // do Server Stuff
      Thread t = 
         new Thread() {
            public void run() {
               doServerStuff();
            }
         };
      t.start();
   }
   
   /** Server action */
   private void doServerStuff() {
      try {
         ServerSocket sSocket = new ServerSocket(SERVER_PORT);
         while(true) {
            Socket cSocket = sSocket.accept();
            Thread t = new ClientThread(cSocket);
            t.start();
         }
      }
      catch(Exception e) {
         Alert alert = new Alert(AlertType.ERROR);
         alert.setContentText(e.toString());
         alert.setTitle("Exception");
         alert.showAndWait();
         System.exit(1);
      }
   }
   
   /** Button handler */
   public void handle(ActionEvent ae) {
      // No button being used for now, probably wont need to
   }
   
   /** Class for a client thread */
   class ClientThread extends Thread {
      Socket cSocket = null;
      Scanner scn = null;
      PrintWriter pwt = null;
      
      /** constructor */
      public ClientThread(Socket _cSocket) {
         cSocket = _cSocket;
      }
      
      /** Thread main */
      public void run() {
         try {
            scn = new Scanner(cSocket.getInputStream());
            pwt = new PrintWriter(cSocket.getOutputStream());
            
            while(scn.hasNextLine()) {
               String command = scn.nextLine();
               switch(command) {
               // 1 of the 3 first cases are received upon Client linking to the server, TAKE case is received when mail sent from client
                  case "BOB":
                     doBob();
                     break;
                  case "JOHN":
                     doJohn();
                     break;
                  case "LUCY":
                     doLucy();
                     break;
                  case "TAKE":
                     takeMail();
                     break;
                  default:
                     taList.appendText("ERROR-Unrecognized command: " + command);
                     break;
               }  // switch
            }  //while
            
            pwt.close();
            scn.close();
            cSocket.close();
            
            taList.appendText("\n221 Bye");
         }  // try
         catch(Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText(e.toString());
            alert.setTitle("Exception");
            alert.showAndWait();
            System.exit(1);
         }
      }  // run
      
      /** doBob */
      private void doBob() {
         globalUser = 1;
         taList.appendText("\n220 BOB Connected");
         
         for(int i = 0; i < bobBox.length; i++) {
         //             if(bobBox[i].getToMail().equals("bob@something.net")) {
         
            pwt.println(bobBox[i]);
            pwt.flush();
         
            taList.appendText("\n250 Ok");
         }
         
         taList.appendText("\n354 End of data with BOB");
      } 
      
      /** doJohn */
      private void doJohn() {
         globalUser = 2;
         taList.appendText("\n220 JOHN Connected");
         
         for(int i = 0; i < johnBox.length; i++) {
         //             if(johnBox[i].getToMail().equals("john@example.com")) {
            pwt.println(johnBox[i]);
            pwt.flush();
         
               
         //             }
            taList.appendText("\n250 Ok");
         }
         
         taList.appendText("\n354 End of data with JOHN");
      } 
      
      /** doLucy */
      private void doLucy() {
         globalUser = 3;
         taList.appendText("\n220 LUCY Connected");
         
         for(int i = 0; i < lucyBox.length; i++) {
         //             if(lucyBox[i].getToMail().equals("lucy@test.org")) {
            pwt.println(lucyBox[i]);
            pwt.flush();
         //             }
            taList.appendText("\n250 Ok");
         }
         
         taList.appendText("\n354 End of data with LUCY");
      }
      
      /** takeMail -- converts the Client's message into an Email object */
      private void takeMail() {
         
         if(globalUser == 1) {
            taList.appendText("\n250 BOB Ok");
         } else if(globalUser == 2) {
            taList.appendText("\n250 JOHN Ok");
         } else if(globalUser == 3) {
            taList.appendText("\n250 LUCY Ok");
         } else {
            taList.appendText("\nERROR: Unknown User");
         }
         String _from = scn.nextLine();
         String _to = scn.nextLine();
         String _date = scn.nextLine();
         String _subject = scn.nextLine();
         String _content = scn.nextLine();
         
      //          System.out.println(_from + " " +  _to + " " + _date + " " + _subject + " " + _content);
         
         Email newEmail = new Email(_from, _to, _date, _subject, _content);
         sendMail(newEmail);
         
      }
      
      /** sendMail -- takes an Email object and directs it to the box it is supposed to go */
      private void sendMail(Email newMail) {
         
         String name = newMail.getToMail();
         
         if(name.equals("bob@something.net")) {
            userTarget = 1;
         } else if(name.equals("lucy@test.org")) {
            userTarget = 3;
         } else if(name.equals("john@example.com")) {
            userTarget = 2;
         }
             
         if(userTarget == 1) {
            taList.appendText("\n250 BOB-RECEIVER Ok");
            bobBox = addMail(bobBox.length, bobBox, newMail);
            
         } else if(userTarget == 2) {
            taList.appendText("\n250 JOHN-RECEIVER Ok");
            johnBox = addMail(johnBox.length, johnBox, newMail);
         
         } else if(userTarget == 3) {
            taList.appendText("\n250 LUCY-RECEIVER Ok");
            lucyBox = addMail(lucyBox.length, lucyBox, newMail);
         
         } else {
            taList.appendText("\nERROR: Unknown Target User");
         }
          
      }
      
      /** addMail -- adds email to Email array box */
      public Email[] addMail(int n, Email mail[], Email x) {
         int i;
         Email newarr[] = new Email[n + 1];
         for(i = 0; i < n; i++) {
            newarr[i] = mail[i];
         }
         newarr[n] = x;     
         return newarr;
         
      }  
      
   }
}