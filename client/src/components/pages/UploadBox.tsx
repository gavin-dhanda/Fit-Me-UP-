import { useState } from "react";
import "../../styles/uploadbox.scss";
import { Category, Formality, Material, Subcategory } from "../items/enums";
import { closebutton, success } from "../../icons/icons";
import { addClothingItem } from "../../utils/api";
import { HexColorPicker } from "react-colorful";

/**
 * Defines the upload box component that allows users to add new items of clothing
 * @param props
 * @returns
 */
export default function UploadBox() {
  const [notSubmitted, setNotSubmitted] = useState<boolean>(true);
  const [clothingType, setClothingType] = useState<number>(-1);
  const [showShapes, setShowShapes] = useState<boolean>(false);
  const [shapeLabels, setShapeLabels] = useState<[string, Subcategory][]>([]); // list of tuples for [label, enum]
  const [showSecondaryColor, setShowSecondaryColor] = useState<boolean>(false);
  const [description, setDescription] = useState<string>("");

  const [subcategory, setSubcategory] = useState<number>(-1);
  const [material, setMaterial] = useState<number>(-1);
  const [formality, setFormality] = useState<number>(-1);

  /**
   * Handles behavior for the x being pressed to close the upload box.
   */
  const handleBoxClose = () => {
    window.location.reload();
  };
  /**
   * Handles behavior for when a type button is pressed.
   * @param type enum for clothing type.
   */
  function handleTypePress(category: Category) {
    // deselects active type button (and shape button) visually
    const activeButton = document.getElementsByClassName("type-active");
    if (activeButton[0]) {
      activeButton[0].className = "inactive";
    }
    const activeShapeButton = document.getElementsByClassName("shape-active");
    if (activeShapeButton[0]) {
      activeShapeButton[0].className = "inactive";
    }

    if (category === clothingType && showShapes) {
      setShowShapes(false);
      setClothingType(-1);
    } else {
      setClothingType(category);
      setShowShapes(true);
      setClothingType(category);

      // selects active button visually
      const buttonName = "type " + category.toString();
      const pressedButton = document.getElementById(buttonName);
      if (pressedButton !== null) {
        pressedButton.className = "type-active";
      }

      // create shape labels based on clothing type
      switch (category) {
        case Category.Top:
          setShapeLabels([
            ["Long Sleeve", Subcategory.LongSleeve],
            ["Short Sleeve", Subcategory.ShortSleeve],
            ["No Sleeve", Subcategory.NoSleeve],
          ]);
          break;
        case Category.Bottom:
          setShapeLabels([
            ["Pants", Subcategory.Pants],
            ["Shorts", Subcategory.Shorts],
            ["Skirt", Subcategory.Skirt],
          ]);
          break;
        case Category.FullBody:
          setShapeLabels([
            ["Dress", Subcategory.Dress],
            ["Romper", Subcategory.Romper],
            ["Suit", Subcategory.Suit],
          ]);
          break;
        case Category.Shoe:
          setShapeLabels([
            ["Sneaker", Subcategory.Sneaker],
            ["Boot", Subcategory.Boot],
            ["Sandal", Subcategory.Sandal],
          ]);
          break;
        case Category.Outerwear:
          setShapeLabels([
            ["Jacket", Subcategory.Jacket],
            ["Sweatshirt", Subcategory.Sweatshirt],
            ["Cardigan", Subcategory.Cardigan],
          ]);
          break;
        case Category.Accessory:
          setShapeLabels([
            ["Headwear", Subcategory.Headwear],
            ["Scarf", Subcategory.Scarf],
            ["Bag", Subcategory.Bag],
          ]);
          break;
      }
    }
  }

  /**
   * Handles behavior for when a shape button is pressed.
   * @param shape enum for shape.
   */
  function handleSubcategorySelection(sub: Subcategory) {
    const activeButton = document.getElementsByClassName("shape-active");
    if (activeButton[0]) {
      activeButton[0].className = "inactive";
    }
    if (subcategory === sub) {
      setSubcategory(-1);
    } else {
      setSubcategory(sub);
      const buttonName = "shape " + sub.toString();
      const pressedButton = document.getElementById(buttonName);
      if (pressedButton !== null) {
        pressedButton.className = "shape-active";
      }
    }
  }

  const [mainColor, setMainColor] = useState<string>("null");
  const [secondaryColor, setSecondaryColor] = useState<string>("null");
  const [mainColorSelect, setMainColorSelect] = useState<string>("Select");
  const [secondaryColorSelect, setSecondaryColorSelect] =
    useState<string>("Select");

  /**
   * Handles behavior for when the color slider is moved.
   * @param color the RGB color the slider is currently on.
   */
  function handleColorChange(color: string, type: "main" | "secondary") {
    if (type === "main") {
      setMainColor(color);
      setMainColorSelect("Select");
    } else {
      setSecondaryColor(color);
      setSecondaryColorSelect("Select");
    }
  }

  /**
   * Function to handle color selection and set the clothing item's fields.
   * @param color the selected RGB color.
   */
  function handleColorSelection(color: string, type: "main" | "secondary") {
    console.log(color);
    if (type === "main") {
      if (mainColorSelect === "Select") {
        mainColor === "null" ? setMainColor("#ffffff") : setMainColor(color);
        setMainColorSelect("Selected!");
        setShowSecondaryColor(true);
      } else {
        setMainColorSelect("Select");
        setShowSecondaryColor(false);
      }
    } else {
      if (secondaryColorSelect === "Select") {
        secondaryColor === "null"
          ? setSecondaryColor("#ffffff")
          : setSecondaryColor(color);
        setSecondaryColorSelect("Selected!");
      } else {
        setSecondaryColor("null");
        setSecondaryColorSelect("Select");
      }
    }
  }

  /**
   * Defines behavior for if a user presses "none" to secondary color.
   */
  const handleNoSecondary = () => {
    setShowSecondaryColor(false);
    setSecondaryColorSelect("Select");
    setSecondaryColor("null");
  };

  /**
   * Handles behavior for when a material type is selected.
   * @param m The material selected.
   */
  function handleMaterialSelection(m: Material) {
    console.log("material button pressed");
    const activeButton = document.getElementsByClassName("material-active");
    console.log("active button is ", activeButton)
    if (activeButton[0]) {
      activeButton[0].className = "inactive";
    }
    if (material === m) {
      setMaterial(-1);
    } else {
      setMaterial(m);
      const buttonName = "material " + m.toString();
      console.log("button name is ", buttonName);
      const pressedButton = document.getElementById(buttonName);
      console.log("pressed button is ", pressedButton)
      if (pressedButton !== null) {
        console.log("setting pressed to active")
        pressedButton.className = "material-active";
      }
    }
  }
  /**
   * Handles behavior for when a formality type is selected.
   * @param f The formality selected.
   */
  function handleFormalitySelection(f: Formality) {
    const activeButton = document.getElementsByClassName("formality-active");
    if (activeButton[0]) {
      activeButton[0].className = "inactive";
    }
    if (formality === f) {
      setFormality(-1);
    } else {
      setFormality(f);
      const buttonName = "formality " + f.toString();
      const pressedButton = document.getElementById(buttonName);
      if (pressedButton !== null) {
        pressedButton.className = "formality-active";
      }
    }
  }

  const [incompleteFields, setIncompleteFields] = useState<boolean>(false); //determines if the user filled out all the fields

  /**
   * Handles submitting a clothing item.
   */
  async function handleSubmit() {
    if (
      clothingType === -1 ||
      subcategory === -1 ||
      mainColor === "null" ||
      material === -1 ||
      formality === -1
    ) {
      setIncompleteFields(true);
    } else {
      setNotSubmitted(false);
      setIncompleteFields(false);
      setMainColorSelect("Select");
      setSecondaryColorSelect("Select");
      setShowShapes(false);
      console.log(description);
      setDescription("");
      // define these local variables because reset doesn't work after the await

      await addClothing(
        clothingType,
        subcategory,
        formality,
        mainColor,
        secondaryColor,
        material,
        description
      );
      console.log(mainColor, secondaryColor);
    }
  }

  /**
   * Calls the backend to add the clothing item to the database.
   * @param category the category of the item.
   * @param subcategory the subcategory of the item.
   * @param formality the formality of the item.
   * @param primary the primary color of the item.
   * @param secondary the secondary color of the item.
   * @param material the material of the item.
   * @param description the description of the item.
   */
  async function addClothing(
    category: number,
    subcategory: number,
    formality: number,
    primary: string,
    secondary: string,
    material: number,
    description: string
  ) {
    // Remove everything but letters from description:
    description = description.replace(/[^a-zA-Z ]/g, "");
    // Capitalize first letter of each word in description:
    description = description.replace(/\b\w/g, (char) => char.toUpperCase());

    await addClothingItem(
      category,
      subcategory,
      formality,
      primary,
      secondary,
      material,
      description
    );
  }

  /**
   * Handles functionality for adding another item from the upload box.
   */
  const addAnotherItem = () => {
    setNotSubmitted(true);
    setMainColor("#ffffff");
    setSecondaryColor("null");
  };

  return notSubmitted ? (
    // prettier-ignore
    <div className="add-box">
      <div className="close-button-container">
        <img className="close-button" src={closebutton} onClick={handleBoxClose} aria-label="Close"/>
      </div>
      <h1 className="add-message"> Add to Closet </h1>
      <div className="types-container">
        <h3 className="clothing-type-header"> Clothing Type:</h3>  {/* All of the category buttons*/}
        <div className="row1">
          <button id={"type " + Category.Top.toString()} className="inactive" onClick={() => handleTypePress(Category.Top)}>Top</button>
          <button id={"type " + Category.Bottom.toString()} className="inactive" onClick={() => handleTypePress(Category.Bottom)}>Bottom</button>
          <button id={"type " + Category.FullBody.toString()} className="inactive" onClick={() => handleTypePress(Category.FullBody)}>Full Body</button>
        </div> 
        <div className="row2">
          <button id={"type " + Category.Shoe.toString()} className="inactive" onClick={() => handleTypePress(Category.Shoe)}>Shoe</button>
          <button id={"type " + Category.Outerwear.toString()} className="inactive" onClick={() => handleTypePress(Category.Outerwear)}>Outerwear</button>
          <button id={"type " + Category.Accessory.toString()} className="inactive" onClick={() => handleTypePress(Category.Accessory)}>Accessory</button>
        </div>
      </div>
      {showShapes && (
        <div className="shapes-container">
          <h3 className="shapes-header"> Subcategory: </h3> {/* All of the subcategory buttons*/}
          <div className="button-container">
            {shapeLabels.map((label) => (
              <button id={`shape ${label[1].toString()}`} className="inactive" onClick={() => handleSubcategorySelection(label[1])}>
                {label[0]}
              </button>
            ))}
          </div>
        </div>
      )}
      <div className="color-container"> {/* The color picker*/}
        <h3>Main Color:</h3>
        <div className="picker-container">
          <HexColorPicker color={mainColor} onChange={(mainColor) => handleColorChange(mainColor, 'main')}/>
          <div className="color-display">
            <div className="color-box" style={{backgroundColor: mainColor}}/>
            <button onClick={() => handleColorSelection(mainColor, 'main')}>{mainColorSelect}</button>
          </div>
        </div>
      </div>
      {showSecondaryColor && (
        <div className="color-container">
          <h3>Secondary Color:</h3>
          <div className="picker-container">
            <HexColorPicker color={secondaryColor} onChange={(secondaryColor) => handleColorChange(secondaryColor, 'secondary')}/>
            <div className="color-display">
              <div className="color-box" style={{backgroundColor: secondaryColor}}/>
              <button onClick={() => handleColorSelection(secondaryColor, 'secondary')}>{secondaryColorSelect}</button>
              <button style={{backgroundColor: "#3f6492", color: "white"}} onClick={handleNoSecondary}>None</button>
            </div>
          </div>
        </div>
      )}
      <div className="material-container"> 
        <h3>Material:</h3> {/* All of the material buttons*/}
        <div className="material-container row1" >
          <button id={"material " + Material.WoolCotton.toString()} className="inactive" onClick={()=>handleMaterialSelection(Material.WoolCotton)}>Cotton/Wool</button>
          <button id={"material " + Material.PlasticNylon.toString()} className="inactive" onClick={()=>handleMaterialSelection(Material.PlasticNylon)}>Synthetic</button>
          <button id={"material " + Material.Leather.toString()} className="inactive" onClick={()=>handleMaterialSelection(Material.Leather)}>Leather</button>
          <button id={"material " + Material.Denim.toString()} className="inactive" onClick={()=>handleMaterialSelection(Material.Denim)}>Denim</button>
        </div>
        <div className="material-container row2" >
          <button id={"material " + Material.SoftFur.toString()} className="inactive" onClick={()=>handleMaterialSelection(Material.SoftFur)}>Fur</button>
          <button id={"material " + Material.StretchySpandex.toString()} className="inactive" onClick={()=>handleMaterialSelection(Material.StretchySpandex)}>Spandex</button>
          <button id={"material " + Material.Other.toString()} className="inactive" onClick={()=>handleMaterialSelection(Material.Other)}>Other</button>
        </div>
      </div>
      <div className="formality-container">
        <h3 > Formality: </h3> {/* All of the formality buttons*/}
        <div className="button-container">
          <button id={"formality " + Formality.Formal.toString()} className="inactive" onClick={() => handleFormalitySelection(Formality.Formal)}>Formal</button>
          <button id={"formality " + Formality.Informal.toString()} className="inactive" onClick={() => handleFormalitySelection(Formality.Informal)}>Informal</button>
          <button id={"formality " + Formality.Flex.toString()} className="inactive" onClick={() => handleFormalitySelection(Formality.Flex)}>Flex</button>
        </div>
      </div>
      <div className="description-container">
        <h3> Optional Description: </h3> {/* The description box*/}
        <textarea className="description-box" placeholder="No special characters!"
        onChange={(ev) => setDescription(ev.target.value.substring(0, Math.min(ev.target.value.length, 40)))} value={description}>
        </textarea>
      </div>
      <button className="add-button" aria-label="submit item" onClick={handleSubmit}>+ Add Item!</button>
      { incompleteFields && <h3 className="incomplete-message"> Please fill out all fields! </h3>}
    </div>
  ) : (
    <div className="add-box">
      <div className="close-button-container">
        <img
          className="close-button"
          src={closebutton}
          onClick={handleBoxClose}
          aria-label="Close"
        />
      </div>
      <h1 className="success-message"> Successfully Added Item!</h1>
      <button className="newitem-button" onClick={addAnotherItem}>
        Add Another Item
      </button>
      <br></br>
      <button className="closet-button" onClick={handleBoxClose}>
        Back To Closet
      </button>
      <br></br>
      <img className="success-icon" src={success} />
    </div>
  );
}
