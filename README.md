## Dragon Curve
Java App that draws dragon curve fractal.
### How to build
```bash
javac  -d  out  src/main/java/com/teazimski/dragoncurve/*.java
```
### How to configure
Open config.yaml file in /src/main/resources and edit the values. For example
```yaml
canvas:
  width: 800  # dynamic or int
  height: 600  # dynamic or int
frame:
  width: 800  # dynamic or int
  height: 600  # dynamic or int
  title: "Dragon Curve"
render:
  color_scheme: "Rainbow" # or just default colors like red, green, blue and etc
  side_length: 3
  sleep_time_ms: 2
```