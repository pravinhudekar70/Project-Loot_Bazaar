import React from "react";

import AliceCarousel from "react-alice-carousel";
import "react-alice-carousel/lib/alice-carousel.css";
import { mainCarouselData } from "./MainCarouselData";

const MainCarousel = () => {
  const items = mainCarouselData.map((item) => (
    <div>
      <img className="cursor-pointer carousel-img" src={item.path} alt=""></img>
    </div>
  ));

  return (
    <AliceCarousel
      mouseTracking
      items={items}
      controlsStrategy="alternate"
      disableButtonsControls
      autoPlay
      autoPlayInterval={1000}
      infinite
    />
  );
};
export default MainCarousel;
