import React, { useState, useEffect } from "react";
import CardComponent from "./components/ui/CardComponent";
import { motion } from "framer-motion";

const cardValues = [1, 2, 3, 5, 8, 13, 21, "?", "☕"];
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

export default function App() {
  const [selectedCard, setSelectedCard] = useState(null);
  const [showResults, setShowResults] = useState(false);
  const [votes, setVotes] = useState([]);
  const [average, setAverage] = useState(null);
  const [userId, setUserId] = useState(null);
  const [isAdmin, setIsAdmin] = useState(false);
  
  console.log(API_BASE_URL)
  useEffect(() => {
    const username = "User1"; // You can set this dynamically
    const adminStatus = true;
    joinSession(username, adminStatus);
  }, []);

  const joinSession = async (username, isAdmin) => {
    const response = await fetch(`${API_BASE_URL}/api/session/join`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, isAdmin })
    });
    const data = await response.json();
    console.log("joined session: " + data.userId + " " + data.isAdmin)
    setUserId(data.userId);
    setIsAdmin(true); // TODO add user management and admin check
  };

  const selectCard = async (value) => {
    if (!showResults) {
      setSelectedCard(value);

      await fetch(`${API_BASE_URL}/api/session/vote`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userId, vote: value })
      });
    }
  };

  const revealCards = async () => {
    if (!isAdmin) {
      alert("Only the admin can reveal votes!");
      return;
    }

    setShowResults(true);

    const response = await fetch(`${API_BASE_URL}/api/session/results/${userId}`);
    const data = await response.json();

    if (data.error) {
      alert(data.error);
      return;
    }

    setVotes(data.votes);
    setAverage(data.average);
  };

  const resetGame = async () => {
    setSelectedCard(null);
    setShowResults(false);
    setVotes([]);
    setAverage(null);

    try {
      const response = await fetch(`${API_BASE_URL}/api/session/reset`, {
        method: "POST"
      });

      if (!response.ok) throw new Error("Failed to reset votes on server");

      console.log("Votes cleared successfully.");
    } catch (err) {
      console.error("Reset failed:", err);
      alert("Failed to reset votes on server.");
    }
  };

  return (
    <div className="flex flex-col items-center p-6 space-y-6">
      <h1 className="text-2xl font-bold">Planning Poker</h1>
      <div className="grid grid-cols-5 gap-4">
        {cardValues.map((value) => (
          <CardComponent
            key={value}
            value={value}
            onClick={() => selectCard(value)}
            selected={selectedCard === value}
          />
        ))}
      </div>
      <button
        onClick={revealCards}
        className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
      >
        Reveal Cards
      </button>

      {showResults && isAdmin && (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="text-xl font-bold mt-4"
        >
          <h2>Votes and Average:</h2>
          <ul className="space-y-2">
            {votes.map(({ userId, vote }) => (
              <li key={userId} className="text-lg">
                User {userId}: {vote}
              </li>
            ))}
          </ul>
          <p className="font-bold mt-4">Average Vote: {average}</p>
        </motion.div>
      )}

      <button
        onClick={resetGame}
        className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded mt-4"
      >
        Reset
      </button>
    </div>
  );
}
