import React from "react";
import { motion } from "framer-motion";

export default function CardComponent({ value, onClick, selected }) {
  return (
    <motion.div whileTap={{ scale: 0.9 }} onClick={onClick} className="cursor-pointer">
      <div
        className={`p-6 text-xl font-bold text-center rounded-2xl shadow-lg transition-all ${
          selected ? "bg-green-500 text-white scale-110" : "bg-blue-500 text-white hover:bg-blue-700"
        }`}
      >
        {value}
      </div>
    </motion.div>
  );
}
