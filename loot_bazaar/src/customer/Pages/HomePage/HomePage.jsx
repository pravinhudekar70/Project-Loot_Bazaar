import React from "react";
import MainCarousel from "../../components/HomeCarousel/MainCarousel";

export default function HomePage() {
  return (
    <>
      <div className="homePage">
        <MainCarousel />
      </div>
      <div>other sections</div>
    </>
  );
}
