find ./src -name "*.java" | xargs javac -d ./target
jar cfe drone_delivery.jar Main -C ./target/ .
