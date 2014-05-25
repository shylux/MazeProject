package com.shylux.java.maze;

import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MazeGui extends Application {
	
	private static int ZOOM = 20;
	private Maze2D model;
	private GraphicsContext gc;
	
	List<Node> lastSolution;
	Node lastStartingPoint;
	
	private GridPane grid = new GridPane();

	@Override
	public void start(Stage stage) throws Exception {
		model = new Maze2D(Paths.get("./mazeData2.csv"));
		
		stage.setTitle("aMAZEing!");

		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25,25,25,25));

		Label xStartLabel = new Label("X:");
		final TextField xStart = new TextField();
		grid.add(xStartLabel, 0, 0);
		grid.add(xStart, 1, 0);
		
		Label yStartLabel = new Label("Y:");
		final TextField yStart = new TextField();
		grid.add(yStartLabel, 0, 1);
		grid.add(yStart, 1, 1);
		
		
	    Label solverLabel = new Label("Solver:");
		ObservableList<IMazeSolver> solvers = FXCollections.observableArrayList( 
		         new MazeSolverRecursive(), new MazeSolverBreadthFirst());
		final ComboBox solver = new ComboBox(solvers);
		solver.setValue(new MazeSolverRecursive());
		grid.add(solverLabel, 0, 2);
		grid.add(solver, 1, 2);

		
	    EventHandler<KeyEvent> textChanged = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				Node startNode = new Node(model, Integer.parseInt(xStart.getText()), Integer.parseInt(yStart.getText()));
				lastSolution = ((IMazeSolver)solver.getValue()).solve(model, startNode);
				drawMaze();
			}
	    };
	    xStart.setOnKeyPressed(textChanged);
	    yStart.setOnKeyPressed(textChanged);

		
		final Canvas canvas = new Canvas(model.getWidth()*ZOOM, model.getHeight()*ZOOM);
		EventHandler<MouseEvent> calcHandler =  new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				int x = (int)((event.getSceneX()-canvas.getLayoutX())/ZOOM);
				int y = (int)((event.getSceneY()-canvas.getLayoutY())/ZOOM);

				Node startNode = model.getNode(x, y);
				if (!startNode.equals(lastStartingPoint)) {
					lastSolution = ((IMazeSolver)solver.getValue()).solve(model, startNode);;
					lastStartingPoint = startNode;
					xStart.setText(Integer.toString(startNode.x));
					yStart.setText(Integer.toString(startNode.y));
				}

				drawMaze();
			}
		};
		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, calcHandler);
		canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, calcHandler);
		gc = canvas.getGraphicsContext2D();
		grid.add(canvas, 0, 3, 2, 1);
		drawMaze();
		
		//grid.setGridLinesVisible(true);
		Scene scene = new Scene(grid);
		
		scene.getStylesheets().add(MazeGui.class.getResource("ressources/mazegui.css").toExternalForm());
		
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}

	public static void launch() {
		Application.launch(new String[] {});
	}
	
	private void drawMaze() {
		gc.save();
		gc.scale(ZOOM/10, ZOOM/10);
		gc.clearRect(0, 0, model.getWidth()*10, model.getHeight()*10);
		gc.setLineWidth( 1 );
		
		if (lastSolution != null && lastSolution.size() > 0) {
			Iterator<Node> iter = lastSolution.iterator();
			Node lastNode, cNode = null;
			do {
				lastNode = cNode;
				cNode = iter.next();
				if (lastNode == null) {
					// first node
					gc.setStroke(Color.GREEN);
					gc.strokeOval(cNode.x*10+2, cNode.y*10+2, 6, 6);
				} else if (iter.hasNext()) {
					// normal path
					gc.setStroke(Color.BLACK);
					gc.strokeLine(lastNode.x*10+5, lastNode.y*10+5, cNode.x*10+5, cNode.y*10+5);
				} else {
					gc.setStroke(Color.BLACK);
					gc.strokeLine(lastNode.x*10+5, lastNode.y*10+5, cNode.x*10+5, cNode.y*10+5);
					gc.setStroke(Color.RED);
					gc.strokeRect(cNode.x*10+2, cNode.y*10+2, 6, 6);
				}
			} while (iter.hasNext());
		}
		
		gc.scale(10, 10);
		gc.setFill(Color.BLACK);
		
		for (int x = 0; x < model.getWidth(); x++) {
			for (int y = 0; y < model.getHeight(); y++) {
				ITile tile = model.getTile(x, y);
				if (!tile.isPassable())
					gc.fillRect(x, y, 1, 1);
			}
		}
		
		
		gc.restore();
		
		
	}
}
