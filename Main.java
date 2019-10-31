package application;
	
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;

public class Main extends Application {
	
	private ArrayList<Player> players = new ArrayList<Player>();
	private final ObservableList<Player> player_list = FXCollections.observableArrayList();
			
	@Override
	public void start(Stage primaryStage) {
		try {
			
			BorderPane root = new BorderPane();
			primaryStage.setTitle("Avalon Stat Tracker");
			Scene scene = new Scene(root, 1200, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			createLayout(root, primaryStage);
			primaryStage.setScene(scene);
			primaryStage.show();
			play();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		final java.net.URL path = getClass().getResource("/avalon.mp3");
        Media media = new Media(path.toString());
        MediaPlayer mp = new MediaPlayer(media);
        mp.play();
	}
	
	@SuppressWarnings("unchecked")
	private void createLayout(BorderPane root, Stage primaryStage) {
		//Creates Top Buttons
		Button newGame = new Button("New Game");
		newGame.setBackground(Background.EMPTY);
		newGame.setPrefSize(200, 40);
		newGame.setId("fancytext");
		Button newPlayer = new Button("New Player");
		newPlayer.setBackground(Background.EMPTY);
		newPlayer.setPrefSize(200, 40);
		newPlayer.setId("fancytext");
		Button editPlayer = new Button("Edit Player");
		editPlayer.setBackground(Background.EMPTY);
		editPlayer.setPrefSize(200,  40);
		editPlayer.setId("fancytext");
		Button deletePlayer = new Button("Delete Player");
		deletePlayer.setBackground(Background.EMPTY);
		deletePlayer.setPrefSize(200, 40);
		deletePlayer.setId("fancytext");
		Button save = new Button("Save");
		save.setBackground(Background.EMPTY);
		save.setPrefSize(200, 40);
		save.setId("fancytext");
		Button load = new Button("Load");
		load.setBackground(Background.EMPTY);
		load.setPrefSize(200, 40);
		load.setId("fancytext");
		HBox buttons = new HBox();
		buttons.getChildren().addAll(newGame, newPlayer, editPlayer, deletePlayer, save , load);
		FadeTransition fade = new FadeTransition(Duration.millis(5000), buttons);
		fade.setFromValue(0);
		fade.setToValue(0.8);
		fade.play();
		root.setTop(buttons);
		//Creates Main stat Display
		//Main Stat Box
		TableView<Player> table = new TableView<Player>();
		table.setEditable(true);
		table.setMaxSize(980, 300);
		TableColumn<Player, String> name = new TableColumn<Player, String>("Name");
		name.setPrefWidth(163);
		name.setCellValueFactory(
				new PropertyValueFactory<Player, String>("name"));
		TableColumn<Player, Float> win_percent = new TableColumn<Player, Float>("Win Percentage");
		win_percent.setPrefWidth(163);
		win_percent.setCellValueFactory(
				new PropertyValueFactory<Player, Float>("win_percentage"));
		TableColumn<Player, Float> win = new TableColumn<Player, Float>("Total Wins");
		win.setPrefWidth(163);
		win.setCellValueFactory(
				new PropertyValueFactory<Player, Float>("win"));
		TableColumn<Player, Float> loss = new TableColumn<Player, Float>("Total Losses");
		loss.setPrefWidth(163);
		loss.setCellValueFactory(
				new PropertyValueFactory<Player, Float>("loss"));
		TableColumn<Player, String> best_char = new TableColumn<Player, String>("Best Character");
		best_char.setPrefWidth(163);
		best_char.setCellValueFactory(
				new PropertyValueFactory<Player, String>("best_char"));
		TableColumn<Player, Float> merlin_ass = new TableColumn<Player, Float>("Merlin Kill %");
		merlin_ass.setPrefWidth(163);
		merlin_ass.setCellValueFactory(
				new PropertyValueFactory<Player, Float>("merlin_kill"));
		table.getColumns().addAll(name, win_percent, win, loss, best_char, merlin_ass);
		table.setItems(player_list);
		VBox box = new VBox();
		box.setPrefSize(500, 400);
		box.setSpacing(5);
		box.setPadding(new Insets(90, 0, 0, 100));
		Label stats = new Label("");
		box.getChildren().addAll(stats, table);
		//Insets insets = new Insets(500);
		//BorderPane.setMargin(root, insets);
		update(table, players);
		if (players.size() != 0) {
			root.setCenter(box);
		}
		//Click on new game
		newGame.setOnMouseClicked(v->{
			new_Game(root, primaryStage, box);
			update(table, players);
		});
		//Click on new player
		newPlayer.setOnMouseClicked(a->{
			newPlayer(table, root, box);
			update(table, players);
		});
		editPlayer.setOnMouseClicked(b->{
			editPlayer(box, root, table);
		});
		deletePlayer.setOnMouseClicked(t->{
			deletePlayer(box, root, table);
		});
		save.setOnMouseClicked(r->{
			save(box, root, table, primaryStage);
		});
		load.setOnMouseClicked(p->{
			load(box, root, table, primaryStage);
		});
		//update(table, players);
	}
	
	public void load(VBox box, BorderPane root, TableView<Player> table, Stage stage) {
		FileChooser pick = new FileChooser();
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		pick.getExtensionFilters().add(filter);
		Scanner scnr = null;
		File chosen = pick.showOpenDialog(stage);
		if (chosen != null) {
			try {
				scnr = new Scanner(chosen);
				if (scnr.hasNext()) {
					while (scnr.nextLine().equals("!")) {
						//scnr.nextLine();
						String name = scnr.nextLine();
						Float wins = Float.valueOf(scnr.nextLine());
						Float loss = Float.valueOf(scnr.nextLine());
						Float mer_loss = Float.valueOf(scnr.nextLine());
						Float mer_win = Float.valueOf(scnr.nextLine());
						Float win_per = Float.valueOf(scnr.nextLine());
						Float mer_per = Float.valueOf(scnr.nextLine());
						String b_char = scnr.nextLine();
						int[] track = new int[8];
						for (int i = 0; i < 8; i++) {
							track[i] = Integer.parseInt(scnr.next());
						}
						Player temp = new Player(name, wins, loss, mer_loss, mer_win, win_per, mer_per, b_char, track);
						players.add(temp);
						player_list.add(temp);
						if (scnr.hasNext()) {
							scnr.nextLine();
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			update(table, players);
			if (players.size() != 0) {
				root.setCenter(box);
			}
			else {
				root.setCenter(null);
			}
		}
		else {
			
		}
	}
	
	public void save(VBox box, BorderPane root, TableView<Player> table, Stage stage) {
		FileChooser get = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		get.getExtensionFilters().add(extFilter);
		FileWriter write = null;
		File chosen = get.showSaveDialog(stage);
		if (chosen != null) {
			try {
				write = new FileWriter(chosen);
				BufferedWriter bwrite = new BufferedWriter(write);
				for (int r = 0; r < players.size(); r++) {
					bwrite.write("!");
					bwrite.newLine();
					bwrite.write(players.get(r).getName());
					bwrite.newLine();
					bwrite.write(Float.toString(players.get(r).getWin()));
					bwrite.newLine();
					bwrite.write(Float.toString(players.get(r).getLoss()));
					bwrite.newLine();
					bwrite.write(Float.toString(players.get(r).getMerlin_loss()));
					bwrite.newLine();
					bwrite.write(Float.toString(players.get(r).getMerlin_win()));
					bwrite.newLine();
					bwrite.write(Float.toString(players.get(r).getWin_percentage()));
					bwrite.newLine();
					bwrite.write(Float.toString(players.get(r).getMerlin_kill()));
					bwrite.newLine();
					bwrite.write(players.get(r).getBest_char());
					bwrite.newLine();
					for (int w = 0; w < 8; w++) {
						bwrite.write(Integer.toString(players.get(r).getCharacters(w)));
						bwrite.write(" ");
					}
					bwrite.newLine();
				}
				bwrite.close();
				Alert emptyAlert = new Alert(AlertType.INFORMATION);
			    emptyAlert.setHeaderText(null);
			    emptyAlert.setTitle(null);
			    emptyAlert.setGraphic(null);
			    emptyAlert.setContentText("File was successfully saved!");
			    emptyAlert.showAndWait();
			}
			catch(FileNotFoundException e) {
				e.printStackTrace();
			}
			catch(IOException r) {
				r.printStackTrace();
			}
			finally {
				try {
					write.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			
		}
	}
	
	public void editPlayer(VBox box, BorderPane root, TableView <Player> table) {
		VBox pane = new VBox();
		Button done = new Button("Done!");
		ObservableList<String> toChange = FXCollections.observableArrayList();
		for (int e = 0; e < players.size(); e++) {
			toChange.add(players.get(e).getName());
		}
		ComboBox<String> toEdit = new ComboBox<String>(toChange);
		pane.getChildren().addAll(toEdit, done);
		root.setCenter(pane);
		done.setOnMouseClicked(y->{
			if (toEdit.getValue() == null || toEdit.getValue().isEmpty()) {
				Alert error = new Alert(AlertType.INFORMATION);
				error.setGraphic(null);
				error.setHeaderText(null);
				error.setTitle("Error!");
				error.setContentText("Please choose a player to edit");
				error.show();
				editPlayer(box, root, table);
			}
			else {
				TextInputDialog new_name = new TextInputDialog();
				new_name.setTitle("Enter player's new name");
				new_name.setGraphic(null);
				new_name.setHeaderText(null);
				new_name.setContentText("New Name:");
				new_name.setOnHidden(i->{
					if (new_name.getResult().isEmpty()) {
						Alert error_two = new Alert(AlertType.INFORMATION);
						error_two.setGraphic(null);
						error_two.setHeaderText(null);
						error_two.setTitle("Error!");
						error_two.setContentText("Player's name cannot be empty!");
						error_two.show();
						editPlayer(box, root, table);
						return;
					}
					for (int t = 0; t < players.size(); t++) {
						if (toEdit.getValue() == (players.get(t).getName())) {
							players.get(t).setName(new_name.getResult());
						}
					}
					root.setCenter(box);
					update(table, players);
				});
			new_name.show();
			}
			update(table, players);
			root.setCenter(box);
		});
	}
	
	public void deletePlayer(VBox box, BorderPane root, TableView<Player> table) {
		VBox pane = new VBox();
		Button delete = new Button("Delete");
		ObservableList<String> toDelete = FXCollections.observableArrayList();
		for (int e = 0; e < players.size(); e++) {
			toDelete.add(players.get(e).getName());
		}
		ComboBox<String> to_Delete = new ComboBox<String>(toDelete);
		pane.getChildren().addAll(to_Delete, delete);
		root.setCenter(pane);
		delete.setOnMouseClicked(p->{
			if (to_Delete.getValue() == null) {
				Alert error = new Alert(AlertType.INFORMATION);
				error.setGraphic(null);
				error.setHeaderText(null);
				error.setTitle("Error!");
				error.setContentText("Please choose a player to delete");
				error.show();
				deletePlayer(box, root, table);
			}
			else {
				String chosen_one = to_Delete.getValue();
				for (int y = 0; y < players.size(); y++) {
					if (players.get(y).getName() == chosen_one) {
						players.remove(y);
					}
				}
			}
			update(table, players);
			if (players.size() != 0) {
				root.setCenter(box);
			}
			else {
				root.setCenter(null);
			}
		});
	}
	
	public static<T> void update(TableView<T> table, List<T> list) {
		table.setItems(null);
		table.layout();
		table.setItems(FXCollections.observableList(list));
	}
	
	
	public HBox createHBox(Label one, Label two, Label three, Label four, Label five, Label six) {
		HBox toRet = new HBox();
		toRet.getChildren().addAll(one, two, three, four, five, six);
		return toRet;
	}
	
	public Label makeLabel(String id) {
		Label toRet = new Label(id);
		toRet.setPrefSize(150, 20);
		return toRet;
	}
	
	public void newPlayer(TableView<Player> table, BorderPane root, VBox box) {
		TextInputDialog player = new TextInputDialog();
		player.setTitle("New Player");
		player.setHeaderText(null);
		player.setGraphic(null);
		player.setContentText("Enter new player's name:");
		player.setOnHidden(d->{
			if (player.getResult().isEmpty()) {
				Alert emptyAlert = new Alert(AlertType.INFORMATION);
			    emptyAlert.setHeaderText(null);
			    emptyAlert.setTitle("Alert");
			    emptyAlert.setGraphic(null);
			    emptyAlert.setContentText("Player name cannot be empty");
			    emptyAlert.showAndWait();
			}
			else {
				Player new_player = new Player(player.getResult());
				players.add(new_player);
				player_list.add(new Player(player.getResult()));
			}
			update(table, players);
			root.setCenter(box);
		});
		player.show();
	}
	
	public void new_Game(BorderPane root, Stage stage, VBox box) {
		String game_players[] = new String[players.size()];
		Player playing[] = new Player[10];
		for (int i = 0; i < players.size(); i++) {
			game_players[i] = players.get(i).getName();
		}
		VBox choice = new VBox();
		for (int i = 0; i < game_players.length; i++) {
			if (game_players[i] == null) {}
			else {
				CheckBox toAdd = new CheckBox(game_players[i]);
				toAdd.setPadding(new Insets(5, 0, 5, 0));
				choice.getChildren().add(toAdd);
				toAdd.setId("boldtext");
			}
		}
		Label message = new Label("Select Players");
		message.setId("boldtext");
		choice.getChildren().add(message);
		Button done = new Button("Done!");
		done.setId("boldtex");
		choice.getChildren().add(done);
		choice.setPrefSize(300, 300);
		choice.setPadding(new Insets(150, 0, 0, 300));
		String style = "-fx-background-color: rgba(255, 255, 255, 0.5);";
		choice.setStyle(style);
		root.setCenter(choice);
		done.setOnAction(e->{
			int counter = 0;
			int new_size = 0;
			for (int i = 0; i < game_players.length; i++) {
				CheckBox temp = (CheckBox)choice.getChildren().get(i);
				if (temp.isSelected()) {
					playing[counter] = players.get(i);
					counter++;
					new_size++;
				}
			}
			Player in_game[] = new Player[new_size];
			for (int b = 0; b < playing.length; b++) {
				if (playing[b] != null) {
					in_game[b] = playing[b];
				}
			}
			match_Characters(in_game, counter, root, box);
		});
	}
	
	public void match_Characters(Player[] in_game, int counter, BorderPane root, VBox box) {
		ObservableList<String> char_options = FXCollections.observableArrayList(
				"Merlin", "Percival", "Assassin", "Mordred", "Morgana", "Oberon", "Normal Blue", "Normal Spy"
		);
		VBox input = new VBox();
		input.setPadding(new Insets(150, 0, 0, 300));
		String style = "-fx-background-color: rgba(255, 255, 255, 0.5);";
		input.setStyle(style);
		
		//Used for character matching to players
		Label one = new Label();
		HBox row_one = new HBox();
		ComboBox<String> usr_one = new ComboBox<String>(char_options);
		Label two = new Label();
		HBox row_two = new HBox();
		ComboBox<String> usr_two = new ComboBox<String>(char_options);
		Label three = new Label();
		HBox row_three = new HBox();
		ComboBox<String> usr_three = new ComboBox<String>(char_options);
		Label four = new Label();
		HBox row_four = new HBox();
		ComboBox<String> usr_four = new ComboBox<String>(char_options);
		Label five = new Label();
		HBox row_five = new HBox();
		ComboBox<String> usr_five = new ComboBox<String>(char_options);
		Label six = new Label();
		HBox row_six = new HBox();
		ComboBox<String> usr_six = new ComboBox<String>(char_options);
		Label seven = new Label();
		HBox row_seven = new HBox();
		ComboBox<String> usr_seven = new ComboBox<String>(char_options);
		Label eight = new Label();
		HBox row_eight = new HBox();
		ComboBox<String> usr_eight = new ComboBox<String>(char_options);
		Label nine = new Label();
		HBox row_nine = new HBox();
		ComboBox<String> usr_nine = new ComboBox<String>(char_options);
		Label ten = new Label();
		HBox row_ten = new HBox();
		ComboBox<String> usr_ten = new ComboBox<String>(char_options);
		
		if (1 <= counter) {
			one.setText(in_game[0].getName());
			one.setMinWidth(100);
			one.setId("boldtext");
			row_one.getChildren().addAll(one, usr_one);
			row_one.setPadding(new Insets(5, 0, 5, 0));
			input.getChildren().add(row_one);
		}
		if (2 <= counter) {
			two.setText(in_game[1].getName());
			two.setMinWidth(100);
			two.setId("boldtext");
			row_two.getChildren().addAll(two, usr_two);
			row_two.setPadding(new Insets(5, 0, 5, 0));
			input.getChildren().add(row_two);
		}
		if (3 <= counter) {
			three.setText(in_game[2].getName());
			three.setMinWidth(100);
			three.setId("boldtext");
			row_three.getChildren().addAll(three, usr_three);
			row_three.setPadding(new Insets(5, 0, 5, 0));
			input.getChildren().add(row_three);
		}
		if (4 <= counter) {
			four.setText(in_game[3].getName());
			four.setMinWidth(100);
			four.setId("boldtext");
			row_four.getChildren().addAll(four, usr_four);
			row_four.setPadding(new Insets(5, 0, 5, 0));
			input.getChildren().add(row_four);
		}
		if (5 <= counter) {
			five.setText(in_game[4].getName());
			five.setMinWidth(100);
			five.setId("boldtext");
			row_five.getChildren().addAll(five, usr_five);
			row_five.setPadding(new Insets(5, 0, 5, 0));
			input.getChildren().add(row_five);
		}
		if (6 <= counter) {
			six.setText(in_game[5].getName());
			six.setMinWidth(100);
			six.setId("boldtext");
			row_six.getChildren().addAll(six, usr_six);
			row_six.setPadding(new Insets(5, 0, 5, 0));
			input.getChildren().add(row_six);
		}
		if (7 <= counter) {
			seven.setText(in_game[6].getName());
			seven.setMinWidth(100);
			seven.setId("boldtext");
			row_seven.getChildren().addAll(seven, usr_seven);
			row_seven.setPadding(new Insets(5, 0, 5, 0));
			input.getChildren().add(row_seven);
		}
		if (8 <= counter) {
			eight.setText(in_game[7].getName());
			eight.setMinWidth(100);
			eight.setId("boldtext");
			row_eight.getChildren().addAll(eight, usr_eight);
			row_eight.setPadding(new Insets(5, 0, 5, 0));
			input.getChildren().add(row_eight);
		}
		if (9 <= counter) {
			nine.setText(in_game[8].getName());
			nine.setMinWidth(100);
			nine.setId("boldtext");
			row_nine.getChildren().addAll(nine, usr_nine);
			row_nine.setPadding(new Insets(5, 0, 5, 0));
			input.getChildren().add(row_nine);
		}
		if (10 <= counter) {
			ten.setText(in_game[9].getName());
			ten.setMinWidth(100);
			ten.setId("boldtext");
			row_ten.getChildren().addAll(ten, usr_ten);
			row_ten.setPadding(new Insets(5, 0, 5, 0));
			input.getChildren().add(row_ten);
		}
		
		ObservableList<String> win_list = FXCollections.observableArrayList(
				"Yes", "No"
		);
		ObservableList<String> mer_killed_list = FXCollections.observableArrayList(
				"Yes", "No", "N/A"
		);
		ComboBox<String> winner = new ComboBox<String>(win_list);
		winner.setPadding(new Insets(5, 0, 5, 0));
		Label win = new Label("Did Resistence Win?");
		win.setPadding(new Insets(5, 0, 5, 0));
		win.setId("boldtext");
		
		
		ComboBox<String> mer_ass = new ComboBox<String>(mer_killed_list);
		mer_ass.setPadding(new Insets(5, 0, 5, 0));
		Label kill = new Label("Did Assassin kill Merlin?");
		kill.setPadding(new Insets(5, 0, 5, 0));
		kill.setId("boldtext");
		
		VBox q1 = new VBox();
		VBox q2 = new VBox();
		
		q1.getChildren().addAll(win, winner);
		q2.getChildren().addAll(kill, mer_ass);
		
		input.getChildren().addAll(q1, q2);
		
		Button done = new Button("Done!");
		done.setId("boldtex");
		done.setPadding(new Insets(5, 0, 5, 0));
		input.getChildren().add(done);

		root.setCenter(input);
		
		done.setOnAction(a-> {
			//Tracking stats
			Boolean res_win;
			Boolean mer_killed;
			if (winner.getValue() == ("Yes")) {
				res_win = true;
			}
			else {
				res_win = false;
			}
			if (mer_ass.getValue() == ("Yes")) {
				mer_killed = true;
			}
			else if (mer_ass.getValue() == ("No")) {
				mer_killed = false;
			}
			else {
				mer_killed = null;
			}
			//Loop through players and track stats
			for (int i = 0; i < in_game.length; i++) {
				ComboBox<String> temp = new ComboBox<String>();
				switch (i) {
				case 0: temp = usr_one;
					break;
				case 1: temp = usr_two;
					break;
				case 2: temp = usr_three;
					break;
				case 3: temp = usr_four;
					break;
				case 4: temp = usr_five;
					break;
				case 5: temp = usr_six;
					break;
				case 6: temp = usr_seven;
					break;
				case 7: temp = usr_eight;
					break;
				case 8: temp = usr_nine;
					break;
				case 9: temp = usr_ten;
					break;
				default: temp = null;
					break;
				}
				String temp_val = (String)temp.getValue();
				if (temp_val == ("Merlin")) {
					if (res_win) {
						in_game[i].setWin(in_game[i].getWin() + 1);
						in_game[i].setCharacters(0, in_game[i].getCharacters(0) + 1);
						in_game[i].setWin_percentage(100 * (in_game[i].getWin() / (in_game[i].getLoss() + in_game[i].getWin())));
					}
					else {
						in_game[i].setLoss(in_game[i].getLoss() + 1);
					}
				}
				else if (temp_val == ("Percival")) {
					if (res_win) {
						in_game[i].setWin(in_game[i].getWin() + 1);
						in_game[i].setCharacters(1, in_game[i].getCharacters(1) + 1);
						in_game[i].setWin_percentage(100 * (in_game[i].getWin() / (in_game[i].getLoss() + in_game[i].getWin())));
					}
					else {
						in_game[i].setLoss(in_game[i].getLoss() + 1);
					}
				}
				else if (temp_val == ("Assassin")) {
					if (res_win) {
						in_game[i].setLoss(in_game[i].getLoss() + 1);
					}
					else {
						in_game[i].setWin(in_game[i].getWin() + 1);
						in_game[i].setCharacters(2, in_game[i].getCharacters(2) + 1);
						in_game[i].setWin_percentage(100 * (in_game[i].getWin() / (in_game[i].getLoss() + in_game[i].getWin())));
					}
					if (mer_killed == null) {
						
					}
					else if (mer_killed) {
						in_game[i].setMerlin_win(in_game[i].getMerlin_win() + 1);
					}
					else {
						in_game[i].setMerlin_loss(in_game[i].getMerlin_loss() + 1);
					}
					in_game[i].setMerlin_kill(100 * (in_game[i].getMerlin_win() / (in_game[i].getMerlin_loss() + in_game[i].getMerlin_win())));
				}
				else if (temp_val == ("Morgana")) {
					if (res_win) {
						in_game[i].setLoss(in_game[i].getLoss() + 1);
					}
					else {
						in_game[i].setWin(in_game[i].getWin() + 1);
						in_game[i].setCharacters(4, in_game[i].getCharacters(4) + 1);
						in_game[i].setWin_percentage(100 * (in_game[i].getWin() / (in_game[i].getLoss() + in_game[i].getWin())));
					}
				}
				else if (temp_val == ("Mordred")) {
					if (res_win) {
						in_game[i].setLoss(in_game[i].getLoss() + 1);
					}
					else {
						in_game[i].setWin(in_game[i].getWin() + 1);
						in_game[i].setCharacters(3, in_game[i].getCharacters(3) + 1);
						in_game[i].setWin_percentage(100 * (in_game[i].getWin() / (in_game[i].getLoss() + in_game[i].getWin())));
					}
				}
				else if (temp_val == ("Oberon")) {
					if (res_win) {
						in_game[i].setLoss(in_game[i].getLoss() + 1);
					}
					else {
						in_game[i].setWin(in_game[i].getWin() + 1);
						in_game[i].setCharacters(5, in_game[i].getCharacters(5) + 1);
						in_game[i].setWin_percentage(100 * (in_game[i].getWin() / (in_game[i].getLoss() + in_game[i].getWin())));
					}
				}
				else if (temp_val == ("Normal Blue")) {
					if (res_win) {
						in_game[i].setWin(in_game[i].getWin() + 1);
						in_game[i].setCharacters(6, in_game[i].getCharacters(6) + 1);
						in_game[i].setWin_percentage(100 * (in_game[i].getWin() / (in_game[i].getLoss() + in_game[i].getWin())));
					}
					else {
						in_game[i].setLoss(in_game[i].getLoss() + 1);
					}
				}
				else if (temp_val == ("Normal Spy")) {
					if (res_win) {
						in_game[i].setLoss(in_game[i].getLoss() + 1);
					}
					else {
						in_game[i].setWin(in_game[i].getWin() + 1);
						in_game[i].setCharacters(7, in_game[i].getCharacters(7) + 1);
						in_game[i].setWin_percentage(100 * (in_game[i].getWin() / (in_game[i].getLoss() + in_game[i].getWin())));
					}
				}
				else {
					Alert error = new Alert(AlertType.INFORMATION);
					error.setContentText("Error Tracking Stats");
					error.setHeaderText(null);
					error.setGraphic(null);
					error.setTitle(null);
					error.show();
				}
			    int tracker = 0;
				for (int b = 1; b < 8; b++) {
					if ( in_game[i].getCharacters(b) > in_game[i].getCharacters(tracker) ) {
						tracker = b;
					}
				}
				Boolean empty = true;
				for (int z = 0; z < 8; z++) {
					if (in_game[i].getCharacters(z) != 0) {
						empty = false;
					}
				}
				if (empty) {
					tracker = -1;
				}
				String toSet = "";
				switch (tracker) {
				case 0: toSet = "Merlin";
					break;
				case 1: toSet = "Percival";
					break;
				case 2: toSet = "Assassin";
					break;
				case 3: toSet = "Mordred";
					break;
				case 4: toSet = "Morgana";
					break;
				case 5: toSet = "Oberon";
					break;
				case 6: toSet = "Normal Blue";
					break;
				case 7: toSet = "Normal Spy";
					break;
				default : toSet = "N/A";
					break;
				}
				in_game[i].setBest_char(toSet);
				in_game[i].setWin_percentage(100 * (in_game[i].getWin() / (in_game[i].getLoss() + in_game[i].getWin())));
			}
			if (players.size() != 0) {
				root.setCenter(box);
			}
			else {
				root.setCenter(null);
			}
		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}