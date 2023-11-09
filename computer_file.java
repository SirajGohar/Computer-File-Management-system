import java.io.*;
import java.util.Scanner;

class ComputerFile {
    private String name;

    public ComputerFile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void open() {
        System.out.println("Opening file: " + name);
    }
}

class TextFile extends ComputerFile {
    public TextFile(String name) {
        super(name);
    }

    public void read() {
        System.out.println("Reading text from " + getName());
    }

    public void write(String content) {
        System.out.println("Writing text to " + getName());
        try (PrintWriter writer = new PrintWriter(new FileWriter(getName(), true))) {
            writer.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ImageFile extends ComputerFile {
    public ImageFile(String name) {
        super(name);
    }

    public void display() {
        System.out.println("Displaying image: " + getName());
    }
}

class User {
    public void performFileOperation(FileManager fileManager) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Options:");
            System.out.println("1. Create a file");
            System.out.println("2. Open a file");
            System.out.println("3. Read from a text file");
            System.out.println("4. Write to a text file");
            System.out.println("5. Display an image file");
            System.out.println("6. List all files");
            System.out.println("7. Delete a file");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter the file name: ");
                    String fileName = scanner.next();
                    System.out.print("Enter the file type (text/image): ");
                    String fileType = scanner.next();
                    ComputerFile createdFile = fileManager.createFile(fileName, fileType);
                    if (createdFile != null) {
                        fileManager.addFile(createdFile);
                    }
                    break;
                case 2:
                    System.out.print("Enter the file name to open: ");
                    String openFileName = scanner.next();
                    ComputerFile file = fileManager.getFileByName(openFileName);
                    if (file != null) {
                        Thread openThread = new Thread(() -> {
                            file.open();
                        });
                        openThread.start();
                    } else {
                        System.out.println("File not found.");
                    }
                    break;
                // Add other cases for reading, writing, listing, and deleting files.
                case 8:
                    System.out.println("Exiting the file manager.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

class FileManager {
    private ComputerFile[] files = new ComputerFile[100]; // Assuming a maximum of 100 files

    private int fileCount = 0;

    public ComputerFile createFile(String name, String type) {
        if (type.equalsIgnoreCase("text")) {
            return new TextFile(name);
        } else if (type.equalsIgnoreCase("image")) {
            return new ImageFile(name);
        } else {
            System.out.println("Unsupported file type");
            return null;
        }
    }

    public void addFile(ComputerFile file) {
        if (fileCount < files.length) {
            files[fileCount] = file;
            fileCount++;
        } else {
            System.out.println("File storage is full.");
        }
    }

    public ComputerFile getFileByName(String name) {
        for (int i = 0; i < fileCount; i++) {
            if (files[i] != null && files[i].getName().equals(name)) {
                return files[i];
            }
        }
        return null;
    }

    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        User user = new User();
        user.performFileOperation(fileManager);
    }
}
