# Fit-Me-UP!

### Portfolio Link
https://gdhanda-portfolio.vercel.app/projects/fitmeup

## Project Overview
#### Fit-Me-UP! is a website designed to encourage users to take advantage of their entire wardrobe with customized outfit suggestions. Our specialized algorithm takes into account color, weather, formality, and generic styling conventions to recommend outfits. Data is saved for every user through their GMail account, allowing users to save their wardrobe and access their favorite outfits across logins! 

### Meet The Team!

We are a group of Sophomores at Brown University passionate about clothing sustainability and style! Our contributions to this project totalled approximately 200 hours. 
#### Asia Nguyen (atnguyen):
Front end styling and functionality, 

#### Eleanor Park (enpark):
Front end component structuring, clothing and outfit functionality, browser zoom formatting, accessibility, and front-end styling.

#### Gavin Dhanda (gdhanda):
Backend API handler development, weather API, back and front-end integration, documentation, and some front-end functionality.

#### Julian Dhanda (jdhanda):
Algorithm development, back-end and front-end testing, some front-end functionality and handler development.

## Launching Fit-Me-UP!
To begin using Fit-Me-UP! follow these steps:

1. Clone the repository from https://github.com/fit-me-up/fitmeup-web.git.
2. In the "client" directory, create a ".env" file with the appropriate firestore values.
3. Run "npm install" from the client directory.
4. In the "server/src/main" directory, create a "resources" folder.
5. Within "resources", create a "firebase_config.json" file with appropriate values.
6. Run "mvn clean package" from the server directory.
7. To start the server, use "./run" from the server directory.
8. To start the webpage, use "npm start" from the client directory.
9. Launch the webpage at http://localhost:8000/.

## Design Choices

### Front-End:

Our website consists of 5 main pages, with the following purpose:

1. Login Page:

The first page shown on launch. Gives the user an introductory message and provides a button for Google Authentication.

2. Home Page: 

The first page shown once logged in. Gives the user a welcome message and directs them to the other pages.


3. Closet Page:

Allows users to add clothing items to their wardrobe, view current clothing items (and sort by category), and remove any unwanted items.

4. Generate Page:

Allows users to generate a new outfit based on the items in their wardrobe—specifying formality—and save the outfit if they would like to wear it again.

5. Saved Page:

Allows users to view previously saved outfits and remove them if they no longer like / want to wear the outfit.

#### Significant Choices:
To allow different pages to share information, the Closet, Generate, and Saved pages all share a UseState with the relevant information for each item of clothing. The information was stored as follows:

    Map<String, [String, String, String, String]>

In this structure, the key was the unique clothing ID for a given item, and the value was a set of four Strings with the following information:

- The type of clothing.
- The filepath to the image representation.
- The hexadecimal primary color.
- The optional image description.

### Back-End:
#### Clothing Handlers:
There are 3 handlers used for clothing manipulation: AddClothing, ListClothing, and RemoveClothing.

All handlers share a storage interface for firebase. The AddClothing handler specifies a unique clothing ID by storing and incrementing the previous ID. This ID is useful for front end visualization and deletion of items. 

In the RemoveClothing handler, all outfits containing the clothing item are also removed.

In the Utils file, methods were added to facilitate conversion from Hex to RGB colors, along with conversions for the clothing representation between 3 forms:
1. "Clothing" Record
2. Comma-Separated String
3. Hash Map of Values

#### Outfit Handlers:

There are 4 handlers used for outfit manipulation: AddOutfit, ListOutfit, RemoveOutfit, and GenerateOutfit.

All handlers share a storage interface for firebase. The AddOutfit handler specifies a unique outfit ID by storing and incrementing the previous ID. This ID is useful for front end visualization and deletion of outfits.

In the Utils file, methods were added to facilitate conversions for the outfit representation between 2 forms:
2. Comma-Separated String
3. Hash Map of Values

The GenerateOutfit handler takes information from the frontend about user preferences and location, WeatherData from the backend, and the Generator built on the backend, to produce an original outfit recommendation for the Front-End.

#### Weather API:

The Weather API functions as a modified version of the CS32 LiveCode, with expanded capabilities, as defined in the WeatherData record.

It is used from the GenerateOutfit Handler to provide weather-based recommendations, and from the Front-End directly to display weather information to the user.

#### Outfit Generation:
Outfit generation involes a complex interaction between weather, formality, color theory, and intuitive style principles.

An outfit is created step by step, with each new item being ranked on its compatibility to the items already in the list. Certain rules determine whether a type of item will be present, but there is always either a top, bottom, and shoe, or full body item and shoe.

When choosing an item for a category, a set of items is sampled randomly from the set of those items in the users closet. Then, for each option, its compatibility is determined with respect to color, weather, and material, and a weighted average is returned to pick the best item to add. Weather and material are fairly straighforward, but for color...

There is a function to approximate the number of shades and hues in the current outfit by breaking down the colors. Based on this, it decides whether to prioritize shade or hue compatibility, while also taking into accound the secondary color if there is one.

## Errors / Bugs
### Front-End:
On the front end, there are no known bugs affecting functionality, but some inconveniences that we hope to improve on in the future. There are some concurrency issues with the display having delayed updates on outfit removal, and the buttons on the generate page do not provide clear indications on whether their functionality was successful.

### Back-End:
Again, while there are no known bugs on the backend, there are some goals for future efficiency that we would hope to implement. First, there are some latency issues with querying the APIs, such that we might hope to implement caching for WeatherData. We also might try and implement more intuitive error handling.

## Testing
Tests -- Explain the testing suites that you implemented for your program and how each test ensures that a part of the program works.

### Running Tests:
Once the repo is cloned and all authentification / configuration files have been set up, you can run both the front and back-end tests from the terminal. 

- On the backend, use "mvn package".
- On the frontent, use "npm run test".
