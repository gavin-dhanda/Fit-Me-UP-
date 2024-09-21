import "../../styles/navbar.scss";
import { useState} from "react";
import sunlogo from "../../styles/images/sunshinefitmeup.png"
import snowylogo from "../../styles/images/snowfitmeup.png"
import rainylogo from "../../styles/images/rainyfitmeup.png"
import cloudylogo from "../../styles/images/cloudyfitmeup.png"
import { getNameCookie } from "../../utils/cookie";
import "../../styles/homepage.scss";
import { useNavigate } from "react-router-dom";
import { WeatherType } from "../items/enums";

/**
 * Class that handles and creates home page features.
 */

/**
 * Function that determines the image to use for the weather.
 * @param weather the type of weather.
 * @returns an icon.
 */
export function mapToImage(weather: WeatherType) {
  switch(weather) {
    case WeatherType.CLEAR:
      return sunlogo;
    case WeatherType.SNOW:
      return snowylogo;
    case WeatherType.CLOUDY:
      return cloudylogo;
    case WeatherType.RAINY:
      return rainylogo;
  }
}

/**
 * Function that determines the type of weather.
 * @param cloud cloud cover percentage.
 * @param rain rain percentage.
 * @param snow snow percentage.
 * @returns what the weather is.
 */
export function determineWeatherIcon(cloud: number, rain: number, snow : number) {
  if (snow > 1) {
    return WeatherType.SNOW;
  } else if (rain > 50) {
    return WeatherType.RAINY
  }else if (cloud > 50) {
    return WeatherType.CLOUDY;
  } else {
    return WeatherType.CLEAR;
  }
}

export default function HomePage() {
  const date = new Date();
  const [showTimeOfDay, setShowTimeOfDay] = useState<boolean>(true);
  const [showName, setShowName] = useState<boolean>(true);
  const [today, setToday] = useState<[number, number]>([0,0]);
  const [messageIndex, setMessageIndex] = useState<number>(0);
  const navigate = useNavigate();

  /**
   * Gets the user's name to display.
   */
  const name = getNameCookie();
  if (name === undefined) {
    setShowName(false);
  }

  /**
   * Gets the time of day.
   * @returns the section of the day in a string to display.
   */
  const getTimeOfDay =  () => {
    const currHour = date.getHours();
    if (currHour >= 5 && currHour < 12) {
      return "Morning";
    } else if (currHour >= 12 && currHour < 18) {
      return "Afternoon";
    } else if (currHour >= 18 && currHour < 24) {
      return "Evening";
    } else {
      setShowTimeOfDay(false);
      return "";
    }
  }

  /**
   * Gets the welcome message.
   * @returns the message to display.
   */
  const getWelcomeMessage = () => {
    if (showTimeOfDay) {
      if (showName) {
        return `Good ${getTimeOfDay()}, ${name}! 😊`;
      } else {
        return `Good ${getTimeOfDay()}! 😊`;
      }
    } else {
      if (showName) {
        return `Hello ${name}! 😊`;
      } else {
        return `Hello! 😊`;
      }
    }
  }

  /**
   * Generates a positive message randomly.
   * @returns the message to display.
   */
  const generatePositiveMessage = () => {
    var i = 0;
    if (date.getDate()===today[0] && date.getMonth()===today[1]) {
      i = messageIndex;
    } else {
      i = Math.floor(Math.random() * 5);
      setMessageIndex(i);
      setToday([date.getDate(), date.getMonth()]);
    }
    switch(i) {
      case 0:
        return "It's nice to see you!";
      case 1:
        return "You're looking great!";
      case 2: 
        return "Excited to see what look you’ll rock today!"
      case 3:
        return "Another great day, another great look!";
      case 4:
        return "You're shining bright today!";
    }
  }

  return (
    <div className="homepage">
      <h1 className="hello-message" aria-label={getWelcomeMessage()}>
        {getWelcomeMessage()}
      </h1>
      <h3 className="positive-message" aria-label={generatePositiveMessage()}>
        {generatePositiveMessage()}
      </h3>
      <div className="nav-buttons">
        <button
          onClick={() => navigate("/generate")}
          style={{ backgroundColor: "#d27088" }}
          aria-label="Go to generate page"
        >
          Generate Today's Outfit{" "}
        </button>
        <button
          onClick={() => navigate("/saved")}
          style={{ backgroundColor: "#846a95" }}
          aria-label="Go to saved page"
        >
          View Saved Outfits{" "}
        </button>
        <button
          onClick={() => navigate("/closet")}
          style={{ backgroundColor: "#3f6492" }}
          aria-label="Go to closet page"
        >
          View My Closet{" "}
        </button>
      </div>
    </div>
  );
}