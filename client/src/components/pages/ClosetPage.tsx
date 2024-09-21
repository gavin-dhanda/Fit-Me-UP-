import {
  all,
  tops,
  bottoms,
  fullbody,
  shoes,
  outerwear,
  accessories,
  trash,
} from "../../icons/icons";
import "../../styles/closetpage.scss";
import { useState, useEffect, Dispatch, SetStateAction } from "react";
import UploadBox from "./UploadBox";
import { removeClothing } from "../../utils/api";
import { Category } from "../items/enums";
import { updateClothing } from "../../utils/api";

/**
 * Class that handles and creates closet page features.
 */

export interface ClosetProps {
  setClothes: Dispatch<
    SetStateAction<Map<string, [string, string, string, string]>>
  >;
  clothes: Map<string, [string, string, string, string]>;
}

export default function ClosetPage(props: ClosetProps) {
  const [showAddBox, setShowAddBox] = useState<boolean>(false);
  const [clothingFilter, setClothingFilter] = useState<string>("All");
  const [opacity, setOpacity] = useState(0.4);
  const [activeButton, setActiveButton] = useState("All");
  const [hoverIndex, setHoverIndex] = useState("-1");

  useEffect(() => {
    updateClothing(props.setClothes);
  }, []);
/**
 * Helper function to highlight the current filter button.
 * @param category the category you clicked on.
 * @returns the appropriate opacity.
 */
  const getButtonOpacity = (category: string) => {
    return activeButton === category.toString() ? 1 : 0.4;
  };

  return (
    <body>
      <div className="selection-bar">
        <img
          onClick={() => {
            /*Filters to show all clothing*/
            setClothingFilter("All");
            setOpacity(1);
            setActiveButton("All");
          }}
          draggable={false}
          src={all}
          aria-label="Show all clothes button"
          alt="Show all clothes"
          style={{ opacity: getButtonOpacity("All") }}
        />
        <img
          onClick={() => {
            /*Filters to show tops*/
            setClothingFilter(Category.Top.toString());
            setOpacity(1);
            setActiveButton(Category.Top.toString());
          }}
          draggable={false}
          src={tops}
          aria-label="Show all tops button"
          alt="Show all tops"
          style={{ opacity: getButtonOpacity(Category.Top.toString()) }}
        />
        <img
          onClick={() => {
            /*Filters to show bottoms*/
            setClothingFilter(Category.Bottom.toString());
            setOpacity(1);
            setActiveButton(Category.Bottom.toString());
          }}
          draggable={false}
          src={bottoms}
          aria-label="Show all bottoms button"
          alt="Show all bottoms"
          style={{ opacity: getButtonOpacity(Category.Bottom.toString()) }}
        />
        <img
          onClick={() => {
            /*Filters to show full body items*/
            setClothingFilter(Category.FullBody.toString());
            setOpacity(1);
            setActiveButton(Category.FullBody.toString());
          }}
          draggable={false}
          src={fullbody}
          alt="Show full body items"
          aria-label="Show all full body items button"
          style={{ opacity: getButtonOpacity(Category.FullBody.toString()) }}
        />
        <img
          onClick={() => {
            /*Filters to show shoes*/
            setClothingFilter(Category.Shoe.toString());
            setOpacity(1);
            setActiveButton(Category.Shoe.toString());
          }}
          draggable={false}
          src={shoes}
          alt="Show all shoes"
          aria-label="Show all shoes button"
          style={{ opacity: getButtonOpacity(Category.Shoe.toString()) }}
        />
        <img
          onClick={() => {
            /*Filters to show outerwear items*/
            setClothingFilter(Category.Outerwear.toString());
            setOpacity(1);
            setActiveButton(Category.Outerwear.toString());
          }}
          draggable={false}
          src={outerwear}
          alt="Show all outerwear"
          aria-label="Show all outerwear button"
          style={{ opacity: getButtonOpacity(Category.Outerwear.toString()) }}
        />
        <img
          onClick={() => {
            /*Filters to show accessories*/
            setClothingFilter(Category.Accessory.toString());
            setOpacity(1);
            setActiveButton(Category.Accessory.toString());
          }}
          draggable={false}
          src={accessories}
          alt="Show all accessories"
          aria-label="Show all accessories button"
          style={{ opacity: getButtonOpacity(Category.Accessory.toString()) }}
        />
        <button onClick={() => setShowAddBox(true)} aria-label="Add item">
          + Add
        </button>
      </div>
      {showAddBox && <UploadBox />}
      {!showAddBox /*Creates each box in the closet to display the appropriate data of the clothing item*/ && (
        <div className="closet-container">
          {Array.from(props.clothes.entries()).map((img, index) =>
            clothingFilter === "All" || img[1][2] === clothingFilter ? (
              <div
                className="box"
                onMouseEnter={() => setHoverIndex(img[0])}
                onMouseLeave={() => setHoverIndex("-1")}
              >
                {hoverIndex === img[0] && img[1][3] !== "" ? (
                  <div className="description">{img[1][3]}</div>
                ) : (
                  <img
                    key={index}
                    src={img[1][0]}
                    alt={img[1][3]}
                    className={"img"}
                    style={{ backgroundColor: img[1][1] }}
                    onClick={() => console.log(img[1][3])}
                  />
                )}
                <img
                  alt="delete item button"
                  className="img-trash" /*Functionality for deletion of clothing item*/
                  src={trash}
                  onClick={() => {
                    removeClothing(parseInt(img[0]));
                    let newClothes = new Map(props.clothes);
                    newClothes.delete(img[0]);
                    props.setClothes(newClothes);
                  }}
                />
              </div>
            ) : null
          )}
        </div>
      )}
    </body>
  );
}
