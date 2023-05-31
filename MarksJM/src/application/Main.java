package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
	private TextField dniTextField;
	private ComboBox<String> cursoComboBox;
	private TableView<Alumno> tableView;
	private TextArea resultadosTextArea;
	private TextField usernameTextField;
	private PasswordField passwordField;
	private Stage primaryStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		showLoginScreen();
	}

	private void showLoginScreen() {
		String imagePath = "\\Users\\34602\\Downloads\\eclipse-workspace\\MarksJM\\res\\beni.jpg";

		Image image = new Image("file:" + imagePath);

		primaryStage.getIcons().add(image);
		primaryStage.setTitle("Login");

		Label usernameLabel = new Label("Username:");
		usernameTextField = new TextField();

		Label passwordLabel = new Label("Password:");
		passwordField = new PasswordField();

		Button loginButton = new Button("Login");
		loginButton.setOnAction(e -> login());

		GridPane loginPane = new GridPane();
		loginPane.setPadding(new Insets(10));
		loginPane.setHgap(10);
		loginPane.setVgap(10);

		loginPane.add(usernameLabel, 0, 0);
		loginPane.add(usernameTextField, 1, 0);
		loginPane.add(passwordLabel, 0, 1);
		loginPane.add(passwordField, 1, 1);
		loginPane.add(loginButton, 1, 2);

		Scene loginScene = new Scene(loginPane);
		primaryStage.setScene(loginScene);
		loginScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.show();
	}

	private void login() {
		String username = usernameTextField.getText();
		String password = passwordField.getText();

		// Aquí debes realizar la verificación de las credenciales en la base de datos
		boolean isAdmin = verifyCredentials(username, password);

		if (isAdmin) {
			showMainWindow();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Invalid username or password.");
			alert.showAndWait();
		}
	}

	private boolean verifyCredentials(String username, String password) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://sql7.freesqldatabase.com/sql7622667",
					"sql7622667", "vYvaFsL32T");
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'");

			if (resultSet.next()) {
				String adminValue = resultSet.getString("admin");
				if ("S".equalsIgnoreCase(adminValue)) {
					return true; // Usuario administrador
				} else if ("N".equalsIgnoreCase(adminValue)) {
					return false; // Usuario no administrador
				}
			}

			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false; // Credenciales inválidas
	}

	private void showMainWindow() {
		String imagePath = "\\Users\\34602\\Downloads\\eclipse-workspace\\MarksJM\\res\\beni.jpg";

		Image image = new Image("file:" + imagePath);

		primaryStage.getIcons().add(image);
		primaryStage.setTitle("Consultoria de notas");

		Label nomLabel = new Label("Nom:");
		TextField nomTextField = new TextField();

		Label primerApellidoLabel = new Label("Primer Cognom:");
		TextField primerCognomTextField = new TextField();

		Label segundoApellidoLabel = new Label("Segón Cognom:");
		TextField segónCognomTextField = new TextField();

		Label dniLabel = new Label("DNI:");
		dniTextField = new TextField();

		Label cursoLabel = new Label("Curs:");
		cursoComboBox = new ComboBox<>();
		cursoComboBox.setItems(FXCollections.observableArrayList("2020-21", "2021-22", "2022-23", "2023-24"));

		Button consultarButton = new Button("Consultar");
		consultarButton.setOnAction((e) -> consultarAlumnos());
		Button afegirAlumneButton = new Button("Afegir alumne");
		afegirAlumneButton.setOnAction(e -> mostrarVentanaAgregarAlumno());
		Button agregarNotasButton = new Button("Agregar Notas");
		agregarNotasButton.setOnAction((e) -> showAgregarNotasScreen());

		tableView = new TableView<>();
		TableColumn<Alumno, String> dniColumn = new TableColumn<>("DNI");
		dniColumn.setCellValueFactory(new PropertyValueFactory<>("dni"));

		TableColumn<Alumno, String> cursoColumn = new TableColumn<>("Curs");
		cursoColumn.setCellValueFactory(new PropertyValueFactory<>("curs"));

		TableColumn<Alumno, String> materiaColumn = new TableColumn<>("Materia");
		materiaColumn.setCellValueFactory(new PropertyValueFactory<>("materia"));

		TableColumn<Alumno, String> ptrimestreColumn = new TableColumn<>("Ptrimestre");
		ptrimestreColumn.setCellValueFactory(new PropertyValueFactory<>("ptrimestre"));

		TableColumn<Alumno, String> strimestreColumn = new TableColumn<>("Strimestre");
		strimestreColumn.setCellValueFactory(new PropertyValueFactory<>("strimestre"));

		TableColumn<Alumno, String> ttrimestreColumn = new TableColumn<>("Ttrimestre");
		ttrimestreColumn.setCellValueFactory(new PropertyValueFactory<>("ttrimestre"));

		// Agregar las columnas a la tabla
		tableView.getColumns().addAll(dniColumn, cursoColumn, materiaColumn, ptrimestreColumn, strimestreColumn,
				ttrimestreColumn);
		resultadosTextArea = new TextArea();
		resultadosTextArea.setEditable(false);

		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10));
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		gridPane.add(nomLabel, 0, 0);
		gridPane.add(nomTextField, 1, 0);
		gridPane.add(primerApellidoLabel, 0, 1);
		gridPane.add(primerCognomTextField, 1, 1);
		gridPane.add(segundoApellidoLabel, 0, 2);
		gridPane.add(segónCognomTextField, 1, 2);
		gridPane.add(dniLabel, 0, 3);
		gridPane.add(dniTextField, 1, 3);
		gridPane.add(cursoLabel, 0, 4);
		gridPane.add(cursoComboBox, 1, 4);
		HBox botonesBox = new HBox(10);
		botonesBox.getChildren().addAll(agregarNotasButton, consultarButton, afegirAlumneButton);

		gridPane.add(botonesBox, 0, 5, 3, 1); // Agregar el HBox en la fila 5, columnas 0 a 2

		gridPane.add(tableView, 0, 6, 2, 1);

		Scene scene = new Scene(gridPane);
		primaryStage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.show();
	}

	private void showAgregarNotasScreen() {
		String imagePath = "\\Users\\34602\\Downloads\\eclipse-workspace\\MarksJM\\res\\beni.jpg";

		Image image = new Image("file:" + imagePath);

		primaryStage.getIcons().add(image);
		Stage stage = new Stage();
		stage.setTitle("Agregar Notas");

		Label dniLabel = new Label("DNI:");
		TextField dniTextField = new TextField();

		Label cursoLabel = new Label("Curs:");
		ComboBox<String> cursoComboBox = new ComboBox<>();
		cursoComboBox.setItems(FXCollections.observableArrayList("2020-21", "2021-22", "2022-23", "2023-24"));

		Label materiaLabel = new Label("Materia:");
		ComboBox<String> materiaComboBox = new ComboBox<>();
		materiaComboBox.setItems(FXCollections.observableArrayList("EIE", "FOL", "PRL", "Programació", "Sis. de gestio",
				"Entorns de des", "BBDD", "Anglès T."));

		Label ptrimestreLabel = new Label("Ptrimestre:");
		TextField ptrimestreTextField = new TextField();

		Label strimestreLabel = new Label("Strimestre:");
		TextField strimestreTextField = new TextField();

		Label ttrimestreLabel = new Label("Ttrimestre:");
		TextField ttrimestreTextField = new TextField();

		Button guardarButton = new Button("Guardar");
		guardarButton.setOnAction((e) -> {
			String dni = dniTextField.getText();
			String curs = cursoComboBox.getValue();
			String materia = materiaComboBox.getValue();
			String ptrimestre = ptrimestreTextField.getText();
			String strimestre = strimestreTextField.getText();
			String ttrimestre = ttrimestreTextField.getText();

			guardarNotas(dni, curs, materia, ptrimestre, strimestre, ttrimestre);
			stage.close();
		});

		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10));
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		gridPane.add(dniLabel, 0, 0);
		gridPane.add(dniTextField, 1, 0);
		gridPane.add(cursoLabel, 0, 1);
		gridPane.add(cursoComboBox, 1, 1);
		gridPane.add(materiaLabel, 0, 2);
		gridPane.add(materiaComboBox, 1, 2);
		gridPane.add(ptrimestreLabel, 0, 3);
		gridPane.add(ptrimestreTextField, 1, 3);
		gridPane.add(strimestreLabel, 0, 4);
		gridPane.add(strimestreTextField, 1, 4);
		gridPane.add(ttrimestreLabel, 0, 5);
		gridPane.add(ttrimestreTextField, 1, 5);
		gridPane.add(guardarButton, 1, 6);

		Scene scene = new Scene(gridPane);
		stage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.show();
	}

	private void mostrarVentanaAgregarAlumno() {
		String imagePath = "\\Users\\34602\\Downloads\\eclipse-workspace\\MarksJM\\res\\beni.jpg";

		Image image = new Image("file:" + imagePath);

		primaryStage.getIcons().add(image);
		Stage stage = new Stage();
		stage.setTitle("Afegir alumne");
		stage.initModality(Modality.APPLICATION_MODAL);

		Label dniLabel = new Label("DNI:");
		TextField dniTextField = new TextField();

		Label nomLabel = new Label("Nom:");
		TextField nomTextField = new TextField();

		Label primerCognomLabel = new Label("Primer cognom:");
		TextField primerCognomTextField = new TextField();

		Label segonCognomLabel = new Label("Segon cognom:");
		TextField segonCognomTextField = new TextField();

		Button afegirButton = new Button("Afegir");
		afegirButton.setOnAction(e -> {
			String dni = dniTextField.getText();
			String nom = nomTextField.getText();
			String primerCognom = primerCognomTextField.getText();
			String segonCognom = segonCognomTextField.getText();

			if (!dni.isEmpty() && !nom.isEmpty() && !primerCognom.isEmpty() && !segonCognom.isEmpty()) {
				if (verificarDNIExistente(dni)) {
					mostrarAlerta(AlertType.ERROR, "Error", "El DNI que has introduït ja existeix.");
				} else if (afegirAlumne(dni, nom, primerCognom, segonCognom)) {
					// Actualizar la tabla de alumnos si se añade correctamente
					consultarAlumnos();
					stage.close();
				} else {
					mostrarAlerta(AlertType.ERROR, "Error", "Error al afegir l'alumne.");
				}
			} else {
				mostrarAlerta(AlertType.ERROR, "Error", "Siusplau, introdueix tots els camps.");
			}
		});

		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10));
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		gridPane.add(dniLabel, 0, 0);
		gridPane.add(dniTextField, 1, 0);
		gridPane.add(nomLabel, 0, 1);
		gridPane.add(nomTextField, 1, 1);
		gridPane.add(primerCognomLabel, 0, 2);
		gridPane.add(primerCognomTextField, 1, 2);
		gridPane.add(segonCognomLabel, 0, 3);
		gridPane.add(segonCognomTextField, 1, 3);
		gridPane.add(afegirButton, 1, 4);

		Scene scene = new Scene(gridPane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		stage.setScene(scene);
		stage.showAndWait();
	}

	private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
		Alert alerta = new Alert(tipo);
		alerta.setTitle(titulo);
		alerta.setHeaderText(null);
		alerta.setContentText(mensaje);
		alerta.showAndWait();
	}

	private boolean verificarDNIExistente(String dni) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://sql7.freesqldatabase.com/sql7622667",
					"sql7622667", "vYvaFsL32T");
			String query = "SELECT dni FROM alumn_regist WHERE dni = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, dni);
			ResultSet resultSet = statement.executeQuery();
			boolean existeDNI = resultSet.next();
			resultSet.close();
			statement.close();
			connection.close();
			return existeDNI;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean afegirAlumne(String dni, String nom, String primerCognom, String segonCognom) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://sql7.freesqldatabase.com/sql7622667",
					"sql7622667", "vYvaFsL32T");
			String query = "INSERT INTO alumn_regist (dni, nom, pcognom, scognom) VALUES (?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, dni);
			statement.setString(2, nom);
			statement.setString(3, primerCognom);
			statement.setString(4, segonCognom);
			int rowsAffected = statement.executeUpdate();
			statement.close();
			connection.close();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void guardarNotas(String dni, String curs, String materia, String ptrimestre, String strimestre,
			String ttrimestre) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://sql7.freesqldatabase.com/sql7622667",
					"sql7622667", "vYvaFsL32T");
			Statement statement = connection.createStatement();
			String query = "INSERT INTO notes (DNI, curs, materia, ptrimestre, strimestre, ttrimestre) " + "VALUES ('"
					+ dni + "', '" + curs + "', '" + materia + "', '" + ptrimestre + "', '" + strimestre + "', '"
					+ ttrimestre + "')";
			statement.executeUpdate(query);

			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean existsDni(String dni) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://sql7.freesqldatabase.com/sql7622667",
					"sql7622667", "vYvaFsL32T");
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM alumn_regist WHERE DNI='" + dni + "'");

			boolean exists = resultSet.next();

			resultSet.close();
			statement.close();
			connection.close();

			return exists;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	private void consultarAlumnos() {
		String dni = dniTextField.getText();
		String curs = cursoComboBox.getValue();
		if (!existsDni(dni)) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("El DNI introduït no existeix. Afegeix el alumna");
			alert.showAndWait();
			return;
		}

		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://sql7.freesqldatabase.com/sql7622667",
					"sql7622667", "vYvaFsL32T");
			Statement myStatement = connection.createStatement();
			ResultSet myResultSet = myStatement
					.executeQuery("SELECT * FROM notes WHERE DNI='" + dni + "'" + "AND curs='" + curs + "'");

			List<Alumno> alumnos = new ArrayList<>();

			while (myResultSet.next()) {
				String dniValue = myResultSet.getString("DNI");
				String cursValue = myResultSet.getString("curs");
				String materiaValue = myResultSet.getString("materia");
				String ptrimestreValue = myResultSet.getString("ptrimestre");
				String strimestreValue = myResultSet.getString("Strimestre");
				String ttrimestreValue = myResultSet.getString("Ttrimestre");

				Alumno alumno = new Alumno(dniValue, cursValue, materiaValue, ptrimestreValue, strimestreValue,
						ttrimestreValue);
				alumnos.add(alumno);
			}

			// Mostrar los resultados en el TableView
			tableView.setItems(FXCollections.observableArrayList(alumnos));

			myResultSet.close();
			myStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Métodos restantes...

	public static class Alumno {
		private String dni;
		private String curs;
		private String materia;
		private String ptrimestre;
		private String strimestre;
		private String ttrimestre;

		public Alumno(String dni, String curs, String materia, String ptrimestre, String strimestre,
				String ttrimestre) {
			this.dni = dni;
			this.curs = curs;
			this.materia = materia;
			this.ptrimestre = ptrimestre;
			this.strimestre = strimestre;
			this.ttrimestre = ttrimestre;
		}

		public String getDni() {
			return dni;
		}

		public void setDni(String dni) {
			this.dni = dni;
		}

		public String getCurs() {
			return curs;
		}

		public void setCurs(String curs) {
			this.curs = curs;
		}

		public String getMateria() {
			return materia;
		}

		public void setMateria(String materia) {
			this.materia = materia;
		}

		public String getPtrimestre() {
			return ptrimestre;
		}

		public void setPtrimestre(String ptrimestre) {
			this.ptrimestre = ptrimestre;
		}

		public String getStrimestre() {
			return strimestre;
		}

		public void setStrimestre(String strimestre) {
			this.strimestre = strimestre;
		}

		public String getTtrimestre() {
			return ttrimestre;
		}

		public void setTtrimestre(String ttrimestre) {
			this.ttrimestre = ttrimestre;
		}
	}
}