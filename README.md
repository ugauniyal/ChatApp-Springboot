# ChatApp - Spring Boot Chat Application

ChatApp is a full-stack chat application built with Spring Boot, allowing users to register, create, update and join channels of interest, and engage in real-time chat within those channels. The project also includes features like email verification, user authentication, and sending top channel updates via email. Below, you'll find details about the project structure, endpoints, and how to get started.

## Table of Contents
- [Project Overview](#project-overview)
- [Project Structure](#project-structure)
- [Endpoints](#endpoints)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Project Overview

This Spring Boot Chat Application provides the following key features:

- User registration with email verification.
- User authentication with token generation.
- Creation and joining of channels worldwide.
- Real-time chat using WebSockets.
- Weekly email notifications of top channels using Quartz.

## Project Structure

The project is organized into several controllers and entities. Here's an overview of the main components:

- **User Controller**: Manages user registration, login, update, and deletion.

- **User Info Controller**: Handles user information display.

- **User Login Controller**: Manages user login and logout.

- **Channel Controller**: Manages channel creation, updating, deletion, and retrieval.

- **Subscribed Channel Controller**: Handles user subscriptions to channels.

- **Message Controller**: Manages real-time messaging within channels.

The project's entities (UserModel, ChannelModel, MessageModel, SubscribedChannelsModel, and others) are not listed here but are integral to the application's functionality.

There are also multiple services such as getting top 10 channels according to message count (ChannelService), sending the verification link to the user's mail (UserService), etc which are also an essential part of the project.

## Endpoints

Here are some of the main endpoints for this application:

- `/createUser`: Register a new user with email verification.
- `/login`: Log in and generate a token.
- `/logout`: Log out.
- `/getChannels`: Get a list of all channels.
- `/subscribe`: Subscribe to a channel.
- `/unsubscribe`: Unsubscribe from a channel.
- `/chat/{channelName}.sendMessage`: Send a message in a specific channel.
- And many more.

For a complete list of endpoints, please refer to the respective controller classes.

## Installation

To run this project locally, you need the following:

1. Java Development Kit (JDK).
2. Spring Boot.
3. A database, such as PostgreSQL.
4. Maven or Gradle for dependency management.

Follow these steps to set up the project:

1. Clone the repository to your local machine.
2. Configure your database connection in `application.properties` or `application.yml`.
3. Build and run the project using your preferred method (Maven or Gradle).

## Usage

After setting up the project, you can interact with the application by making HTTP requests to the provided endpoints. You can use tools like Postman or curl to test the endpoints and explore the chat functionality.

## Contributing

Contributions to this project are welcome. Feel free to submit issues or pull requests if you find any bugs or have suggestions for improvements.

## License

This project is licensed under the MIT License.

---

For more information and detailed instructions, please refer to the project's [GitHub repository](https://github.com/ugauniyal/ChatApp-Springboot).

Feel free to reach out if you have any questions or need further assistance.

Happy chatting!
