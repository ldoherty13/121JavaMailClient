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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * MailServer - Acts as a mail server
 * Name: Luke Doherty Tim Lonergan Andrew Susz
 * Course/Section: ISTE-121-02
 * Final Group Project
 * Date: 12/6/2021
 */
 
public class MailClient extends Application implements EventHandler<ActionEvent> 
{
   // Window attributes
   private Stage stage;
   private Scene scene;
   private String userChoice = "";
   private String userEmail = "";
   Socket soc = null;
   
   // Declare GUI Objects and the mail function ports here
   private Label labelSender = new Label("To:       ");
   private Label labelReceiver = new Label("From:   ");
   private Label labelSubject = new Label("Subject: ");
   private TextField senderField = new TextField();
   private TextField receiverField = new TextField();
   private TextField subjectField = new TextField();
   private TextArea mailBodyArea = new TextArea();
   private TextArea messageArea = new TextArea();
   private Button buttonSend = new Button("Send");
   private Button buttonCompose = new Button("Create New Message");
   private Menu menu = new Menu("Select One");
   private MenuBar mbOne = new MenuBar();
   private MenuItem menuBob = new MenuItem("Bob");
   private MenuItem menuJohn = new MenuItem("John");
   private MenuItem menuLucy = new MenuItem("Lucy");
   
   // Static items
   private Socket socket = null;
   private static final int SERVER_PORT = 35353;
   private PrintWriter pwt = null;
   private Scanner scn = null;
   
   
   /**
    * Main program ... 
    * @args - command line arguments (ignored)
    */
   public static void main(String[] args) 
   {
      launch(args);
   }
   
   /**
    * Constructor ... draws the GUI
    */
  
  //Sets stage, window titles, and adds all GUI components to the scene
   public void start(Stage _stage) 
   {
      stage = _stage;
   
      
      VBox root1 = new VBox(8);
      FlowPane fpMain = new FlowPane(8.0D, 8.0D);
      
      stage.setTitle("Select User");
      fpMain.setAlignment(Pos.CENTER);
      
      menu.getItems().add(menuBob);
      menu.getItems().add(new SeparatorMenuItem());
      menu.getItems().add(menuLucy);
      menu.getItems().add(new SeparatorMenuItem());
      menu.getItems().add(menuJohn);
      
      mbOne.getMenus().add(menu);
      
      fpMain.getChildren().addAll(mbOne);
   
      
      root1.getChildren().addAll(fpMain);
      
      menuJohn.setOnAction(
         e -> {
            userChoice = "john";
            userEmail = "john@example.com";
            doLogin();
         });
      
      menuLucy.setOnAction(
         e -> {
            userChoice = "lucy";
            userEmail = "lucy@test.org";
            doLogin();
         });
      
      menuBob.setOnAction(
         e -> {
            userChoice = "bob";
            userEmail = "bob@something.net";
            doLogin();
         });
      
      scene = new Scene((Parent)root1, 250, 80);
      stage.setScene(scene);
      stage.show();
            
   }
   
   public void handle(ActionEvent evt) 
   {
      Button btn = (Button)evt.getSource();
      
      
      switch(btn.getText()) 
      {
            
         case "Create New Message":
            doCompose();
            break;
            
         case "Send":
            doSend();
            break;
            
      }
   
   }
   // connect to the server automatically when user is selected
   private void doConnect() {
      try {
         socket = new Socket("localhost", SERVER_PORT);
         pwt = new PrintWriter(socket.getOutputStream());
         scn = new Scanner(socket.getInputStream());
         
         pwt.println(userChoice.toUpperCase());
         pwt.flush();
         
      } catch(Exception e) {
         Alert alert = new Alert(AlertType.ERROR);
         alert.setTitle("Exception");
         alert.setContentText(e.toString());
         alert.showAndWait();
         System.exit(1);
      }
   }
   // Sends the formated email back to the server line by line for it to read
   private void doSend() {
      pwt.println("TAKE");
      pwt.flush();
      
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
      LocalDateTime now = LocalDateTime.now();     
      
      String from, to, date, subject, content = "";
      from = receiverField.getText();
      to = senderField.getText();
      date = dtf.format(now);
      subject = subjectField.getText();
      content = messageArea.getText();
      
      pwt.println(from);
      pwt.flush();
      
      pwt.println(to);
      pwt.flush();
      
      pwt.println(date);
      pwt.flush();
      
      pwt.println(subject);
      pwt.flush();
      
      pwt.println(content);
      pwt.flush();
   }
   // after they select a user, create the mailbox gui and connect
   // with the server and pulls mail but doesn't work if they actually have mail.
   private void doLogin() {
      doConnect();
      getMail();
      this.stage.setOnCloseRequest(
         new EventHandler<WindowEvent>() 
         {
            public void handle(WindowEvent evt) 
            {
               System.exit(0);
            }
         }); 
      
      VBox root3 = new VBox(8);
      FlowPane fpMain = new FlowPane(8.0D, 8.0D);
      
      stage.setTitle("Mailbox");
      fpMain.setAlignment(Pos.CENTER);
      fpMain.getChildren().addAll(buttonCompose);
      root3.getChildren().add(fpMain);
    
      mailBodyArea.setPrefHeight(300);
      
      root3.getChildren().add(mailBodyArea);
    
      buttonCompose.setOnAction(this);
    
      scene = new Scene((Parent)root3, 500, 400);
      stage.setScene(scene);
      stage.show();
   }
   
   // THIS IS THE PART WE COULDN'T GET TO WORK 
   // (Reference doJohn(), doBob(), doLucy() in MailServer.
   private void getMail() {
   
   //             String temp = scn.nextLine();
   //           if(temp.equals(null)) {
   //              mailBodyArea.setText("");
   //           } else if(!temp.equals(null)) {
   //             mailBodyArea.appendText("\n" + temp);
   //           }
      mailBodyArea.setText(scn.nextLine());
   
   }
   
   
   // This builds the "send email" interface.
   private void doCompose()
   {
      VBox root2 = new VBox(8);
      
      FlowPane newWinRow3 = new FlowPane(8.0D, 8.0D);
      newWinRow3.setAlignment(Pos.BASELINE_LEFT);
      receiverField.setPrefColumnCount(30);
      receiverField.setPrefWidth(150);
      receiverField.setEditable(false);
      receiverField.setDisable(true);
      receiverField.setText(userEmail);
      newWinRow3.getChildren().addAll(labelReceiver, receiverField);
      root2.getChildren().add(newWinRow3);
      
      FlowPane newWinRow2 = new FlowPane(8.0D, 8.0D);
      newWinRow2.setAlignment(Pos.BASELINE_LEFT);
      receiverField.setPrefColumnCount(30);
      newWinRow2.getChildren().addAll(labelSender, senderField);
      root2.getChildren().add(newWinRow2);
      
      FlowPane newWinRow4 = new FlowPane(8.0D, 8.0D);
      newWinRow4.setAlignment(Pos.BASELINE_LEFT);
      receiverField.setPrefColumnCount(30);
      newWinRow4.getChildren().addAll(labelSubject, subjectField);
      root2.getChildren().add(newWinRow4);
      
      FlowPane newWinRow5 = new FlowPane(8.0D, 8.0D);
      newWinRow5.setAlignment(Pos.BASELINE_LEFT);
      messageArea.setPrefHeight(150);
      newWinRow5.getChildren().addAll(messageArea);
      root2.getChildren().add(newWinRow5);
      
      FlowPane newWinRow1 = new FlowPane(8.0D, 8.0D);
      newWinRow1.setAlignment(Pos.CENTER);
      receiverField.setPrefColumnCount(30);
      newWinRow1.getChildren().addAll(buttonSend);
      root2.getChildren().add(newWinRow1);
      
      buttonSend.setOnAction(this);
      
      Stage stage2 = new Stage();
      stage2.setTitle("New Message");
      stage2.setScene(new Scene(root2, 450, 320));
      stage2.show();
      
      
   }
}