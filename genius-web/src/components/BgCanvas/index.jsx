import React, { useEffect, useRef } from "react";
// import { initCanvas } from "./init";
import { initCanvas } from "./init2";
const BgCanvas = () => {
  const initState = useRef(null);
  useEffect(() => {
    if (!initState.current) {
      initState.current = initCanvas();
    }
  }, []);
  return <canvas id="scene"></canvas>;
};

export default BgCanvas;
