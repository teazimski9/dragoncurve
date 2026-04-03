package config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Config {
    private int canvasWidth = 2048;
    private int canvasHeight = 1152;
    private int frameWidth = 1280;
    private int frameHeight = 720;
    private int sideLength = 5;
    private int sleepTimeMs = 2;
    private String frameTitle = "Dragon";
    private boolean isDynamicFrame = false;
    private String colorScheme;

    public Config() {
        loadConfig();
    }

    private int parseDimension(String value, boolean isWidth) {
        if (value.equalsIgnoreCase("auto") || value.equalsIgnoreCase("dynamic")) {
            isDynamicFrame = true;
            return 200; // start initially small and grow dynamically
        }
        return Integer.parseInt(value);
    }

    private void loadConfig() {
        try {
            java.io.InputStream is = Config.class.getResourceAsStream("/config.yaml");
            if (is == null) {
                Path path = Paths.get("src/main/resources/config.yaml");
                if (Files.exists(path)) {
                    is = Files.newInputStream(path);
                } else {
                    path = Paths.get("config.yaml");
                    if (Files.exists(path)) {
                        is = Files.newInputStream(path);
                    } else {
                        return;
                    }
                }
            }
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
            List<String> lines = new java.util.ArrayList<>();
            String lineRead;
            while ((lineRead = reader.readLine()) != null)
                lines.add(lineRead);
            is.close();
            String currentSection = "";
            for (String line : lines) {
                String trimmed = line.split("#")[0].trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                if (!line.startsWith(" ") && trimmed.endsWith(":")) {
                    currentSection = trimmed.substring(0, trimmed.length() - 1);
                } else if (trimmed.contains(":")) {
                    String[] parts = trimmed.split(":", 2);
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                    } else if (value.startsWith("'") && value.endsWith("'")) {
                        value = value.substring(1, value.length() - 1);
                    }
                    String fullKey = currentSection.isEmpty() ? key : currentSection + "." + key;
                    switch (fullKey) {
                        case "canvas.width":
                            canvasWidth = parseDimension(value, true);
                            break;
                        case "canvas.height":
                            canvasHeight = parseDimension(value, false);
                            break;
                        case "frame.width":
                            frameWidth = parseDimension(value, true);
                            break;
                        case "frame.height":
                            frameHeight = parseDimension(value, false);
                            break;
                        case "frame.title":
                            frameTitle = value;
                            break;
                        case "render.color_scheme":
                            colorScheme = value;
                            break;
                        case "render.side_length":
                            sideLength = Integer.parseInt(value);
                            break;
                        case "render.sleep_time_ms":
                            sleepTimeMs = Integer.parseInt(value);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to read config.yaml: " + e.getMessage());
        }
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public String getColorScheme() {
        return colorScheme;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public int getSideLength() {
        return sideLength;
    }

    public int getSleepTimeMs() {
        return sleepTimeMs;
    }

    public String getFrameTitle() {
        return frameTitle;
    }

    public boolean isDynamicFrame() {
        return isDynamicFrame;
    }
}
