![project_name (3)](https://user-images.githubusercontent.com/66903296/233828533-91c2a683-75b4-4c3d-8c41-af0bedbab1dd.png)
# Sketch Transfer Server Side

## Description
Welcome to the server-side of the Sketch Transfer app! This is a Spring Boot application that uses HTTP authorization and WebSocket client-to-client connection to facilitate real-time collaboration on sketches.

The app also incorporates a JWT token authorization system for secure user authentication.

To use this app, you'll need to have a compatible client-side app installed. You can find the client-side code for the [Sketch Transfer app](https://github.com/node-value/sketch_transfer).

Once you have both the client and server-side apps up and running, you'll be able to collaborate with others on sketches in real-time. Happy sketching!

**Note: For now colaboration works only if server runs locally.**

## Build
- Clone the repository to your local machine using `git clone <repo url>`.
- Open a terminal and navigate to the root directory of the project.
- Build the project by running the following command: `mvn clean install`.
- After the build is complete, navigate to the target folder that was created in the root directory.
- Start the server by running the following command: `java -jar sketch-transfer-server-<version>.jar`
-The server should now be running on `http://localhost:8080`.

## Note for Challange Reviwers
The security, authorization, and data management code of this app were adapted from my previous study project: [web_lab_4](https://github.com/node-value/web-lab4-back)
