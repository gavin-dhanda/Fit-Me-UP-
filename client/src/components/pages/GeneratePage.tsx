import { generateOutfit, addOutfit } from "../../utils/api";
import { OutfitItem } from "../items/OutfitItem";
import { Dispatch, useState, SetStateAction } from "react";
import { dresser } from "../../icons/icons";
import "../../styles/generatepage.scss";

/**
 * Class that handles and creates generate page features.
 */
export interface GenerationProps {
  clothes: Map<string, [string, string, string, string]>;
}

/**
 * Function that sets up a visualized clothing item with the appropriate data.
 * @param item the clothing item to display.
 * @param props the passed in map of clothing items.
 * @param hoverIndex index for the description.
 * @param outfitID id of the outfit.
 * @returns a visual of the item.
 */
export function showClothing(
  item: string,
  props: Map<string, [string, string, string, string]>,
  hoverIndex: string,
  outfitID: boolean
) {
  let img = props.get(item);
  return item !== "-1" ? (
    <div>
      {((hoverIndex === item && img?.[3] !== "" && outfitID) ? <div className="description">{img?.[3]}</div> : 
                
      <img
        className="img-outfit"
        key={0}
        src={props.get(item)?.[0]}
        alt="Marker"
        style={{ backgroundColor: props.get(item)?.[1] }}
      />)}
    </div>
  ) : null;
}

export default function GeneratePage(props: GenerationProps) {
  const [outfit, setOutfit] = useState<OutfitItem>(new OutfitItem());
  const [text, setText] = useState(1);
  const texts = ["Formal", "Informal", "Flex"];
  const [hov, setHov] = useState("-1");

  /**
   * Toggles formality button.
   */
  const toggleText = () => {
    setText((text + 1) % texts.length);
  };

  /**
   * Calls backend to generate an outfit and updates the usestate.
   * @param setOutfit the outfit usestate.
   */
  async function generateNewOutfit(
    setOutfit: Dispatch<SetStateAction<OutfitItem>>
  ) {
    generateOutfit(text).then((outfit: { outfit: OutfitItem }) => {
      setOutfit(outfit.outfit);
    });
  }

  /**
   * Calls on the api from the frontend, passing in the newly generated outfit's details.
   */
  async function saveOutfit() {
    // Check that the outfit is not empty.
    if (
      outfit.top === "-1" &&
      outfit.bottom === "-1" &&
      outfit.shoe === "-1" &&
      outfit.outerwear === "-1" &&
      outfit.fullbody === "-1" &&
      outfit.accessory === "-1"
    ) {
      return;
    }
    await addOutfit(
      outfit.top,
      outfit.bottom,
      outfit.shoe,
      outfit.outerwear,
      outfit.fullbody,
      outfit.accessory
    );
  }

  /**
   * Function that determines the layout of the generated outfit.
   * @param category the category of the item.
   * @returns appropriate layout to display.
   */
  function determineBox(category: string) {
    if (category != "-1") { // Check to see if the item is a fullbody
      return (
        <div>
          <div
            className="fullbody-box"
            onMouseEnter={() => setHov(outfit.fullbody)}
            onMouseLeave={() => setHov("-1")}
          >
            {showClothing(outfit.fullbody, props.clothes, hov, true)}
          </div>
          <div
            className="shoe-box"
            onMouseEnter={() => setHov(outfit.shoe)}
            onMouseLeave={() => setHov("-1")}
          >
            {showClothing(outfit.shoe, props.clothes, hov, true)}
          </div>
        </div>
      );
    } else { // If the item is a regular outfit (top and bottom)
      return (
        <div>
          <div
            className="top-box"
            onMouseEnter={() => setHov(outfit.top)}
            onMouseLeave={() => setHov("-1")}
          >
            {showClothing(outfit.top, props.clothes, hov, true)}
          </div>
          <div
            className="bottom-box"
            onMouseEnter={() => setHov(outfit.bottom)}
            onMouseLeave={() => setHov("-1")}
          >
            {showClothing(outfit.bottom, props.clothes, hov, true)}
          </div>
          <div
            className="shoe-box"
            style={{ top: "70%", left: "65%" }}
            onMouseEnter={() => setHov(outfit.shoe)}
            onMouseLeave={() => setHov("-1")}
          >
            {showClothing(outfit.shoe, props.clothes, hov, true)}
          </div>
        </div>
      );
    }
  }

  return (
    <body>
      <div className="outfit-container">
        <div className="dresser-container">
          <img className="img-dresser" src={dresser} />
          <div
            className="outerwear-box"
            onMouseEnter={() => setHov(outfit.outerwear)}
            onMouseLeave={() => setHov("-1")}
          >
            {showClothing(outfit.outerwear, props.clothes, hov, true)}
          </div>
          <div
            className="accessory-box"
            onMouseEnter={() => setHov(outfit.accessory)}
            onMouseLeave={() => setHov("-1")}
          >
            {showClothing(outfit.accessory, props.clothes, hov, true)}
          </div>
        </div>
        {determineBox(outfit.fullbody)}
        <div className="left-button-container">
          <button /* Button to generate an outfit*/
            aria-label="Generate button"
            className="generate-btn"
            onClick={async () => generateNewOutfit(setOutfit)}
          >
            Generate ⚙
          </button>
          <button className="type-btn" aria-label="Type button" onClick={toggleText}> {/* Button to specify formality*/}
            {texts[text]}
          </button>
        </div>
        <div className="right-button-container">
          <button className="save-btn" aria-label="Save button" onClick={async () => saveOutfit()}> {/* Button to save an outfit*/}
            Save ⭑
          </button>
        </div>
      </div>
    </body>
  );
}
