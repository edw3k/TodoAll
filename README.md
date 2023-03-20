## **TodoALL - Notes and Tasks App**


TodoALL is a simple Android app that allows users to keep track of their notes and tasks. The app features a todo list, a chronometer and a note-taking screen.
## Features


- Todo list: The first screen of the app shows the todo list, which is populated with tasks from both the API and the local DB. Users can add, edit and delete tasks, and mark them as completed.
- Chronometer: The chronometer screen allows users to start, pause and restart the time, and also record lap times. This screen is useful for timing various activities or events.
- Notes: The notes screen allows users to create and save notes, which can be edited or deleted later.

## Architecture and Persistence

The app is built using the Model-View-ViewModel (MVVM) architecture, which separates the UI from the business logic and data. The app also uses the Room persistence library
to store data locally on the device and retrofit to downloand the tasks from the json of our API
