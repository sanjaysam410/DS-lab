package com.example.bookshelf;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class PresentationTier extends Application {

    private ApplicationLogic appLogic;
    private ListView<String> itemList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        appLogic = new ApplicationLogic();

        primaryStage.setTitle("3-Tier App for BookShelf");

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding:12");
        Button addButton = new Button("Add Book");
        Button listButton = new Button("List Books");
        Button deleteButton = new Button("Delete Selected");
        itemList = new ListView<>();

        addButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Book");
            dialog.setHeaderText("Enter Book name:");
            dialog.setContentText("Book Name:");
            dialog.showAndWait().ifPresent(item -> {
                if (item == null || item.isEmpty() || item.trim().isEmpty()) {
                    showAlert("Validation", "Book name cannot be empty.");
                    return;
                }
                addItemAsync(item.trim());
            });
        });

        listButton.setOnAction(e -> loadItemsAsync());

        deleteButton.setOnAction(e -> {
            String selected = itemList.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Delete Error", "Please select a book to delete.");
                return;
            }
            // expected format: "id - name"
            String[] parts = selected.split("-", 2);
            if (parts.length < 1) {
                showAlert("Delete Error", "Invalid item selected.");
                return;
            }
            String idStr = parts[0].trim();
            int id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException ex) {
                showAlert("Delete Error", "Cannot parse item id.");
                return;
            }
            deleteItemAsync(id);
        });

        // add controls once (no duplicates)
        layout.getChildren().addAll(addButton, listButton, deleteButton, itemList);

        Scene scene = new Scene(layout, 360, 280);
        primaryStage.setScene(scene);
        primaryStage.show();

        // load initial items
        loadItemsAsync();
    }

    private void addItemAsync(final String name) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                appLogic.addItem(name);
                return null;
            }
        };
        task.setOnSucceeded(e -> loadItemsAsync());
        task.setOnFailed(e -> {
            final Throwable ex = task.getException();
            ex.printStackTrace();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    showAlert("DB Error", ex.getMessage());
                }
            });
        });
        new Thread(task).start();
    }

    private void loadItemsAsync() {
        Task<List<String>> task = new Task<List<String>>() {
            @Override
            protected List<String> call() throws Exception {
                return appLogic.getItems();
            }
        };
        task.setOnSucceeded(e -> itemList.getItems().setAll(task.getValue()));
        task.setOnFailed(e -> {
            final Throwable ex = task.getException();
            ex.printStackTrace();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    showAlert("DB Error", ex.getMessage());
                }
            });
        });
        new Thread(task).start();
    }

    private void deleteItemAsync(final int id) {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return appLogic.deleteById(id);
            }
        };

        task.setOnSucceeded(e -> {
            Boolean deleted = task.getValue();
            if (deleted != null && deleted) {
                loadItemsAsync();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showAlert("Delete", "Item deleted successfully.");
                    }
                });
            } else {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showAlert("Delete", "Item not found or could not be deleted.");
                    }
                });
            }
        });

        task.setOnFailed(e -> {
            final Throwable ex = task.getException();
            ex.printStackTrace();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    showAlert("DB Error", ex.getMessage());
                }
            });
        });

        new Thread(task).start();
    }

    private void showAlert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
