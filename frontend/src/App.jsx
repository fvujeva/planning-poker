import React, { useState, useEffect } from "react";
import CardComponent from "./components/ui/CardComponent";
import { motion } from "framer-motion";

const cardValues = [1, 2, 3, 5, 8, 13, 21, "?", "☕"];
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

export default function App() {
  const [username, setUsername] = useState("");
  const [hasJoined, setHasJoined] = useState(false);
  const [selectedCard, setSelectedCard] = useState(null);
  const [showResults, setShowResults] = useState(false);
  const [votes, setVotes] = useState([]);
  const [average, setAverage] = useState(null);
  const [userId, setUserId] = useState(null);
  const [isAdmin, setIsAdmin] = useState(false);
  const [sessionId, setSessionId] = useState(null);

  useEffect(() => {
    const storedSession = localStorage.getItem("planningPokerUser");
    const params = new URLSearchParams(window.location.search);
    const sessionFromUrl = params.get("sessionId");

    if (sessionFromUrl) {
      setSessionId(sessionFromUrl);
    }

    if (storedSession) {
      const { userId, username, isAdmin, sessionId } = JSON.parse(storedSession);
      setUserId(userId);
      setUsername(username);
      setIsAdmin(isAdmin);
      setHasJoined(true);
      setSessionId(sessionId);
    }
  }, []);

  const handleJoin = async () => {
    if (!username) {
      alert("Please enter a username");
      return;
    }

    try {
      const response = await fetch(`${API_BASE_URL}/api/session/join`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, isAdmin: false, sessionId }),
      });

      const data = await response.json();
      setUserId(data.userId);
      setIsAdmin(data.isAdmin);
      setHasJoined(true);
      setSessionId(data.sessionId);

      localStorage.setItem(
        "planningPokerUser",
        JSON.stringify({
          userId: data.userId,
          username,
          isAdmin: data.isAdmin,
          sessionId: data.sessionId,
        })
      );
    } catch (err) {
      console.error("Join failed:", err);
      alert("Failed to join session.");
    }
  };

  const handleCreate = async () => {
    if (!username) {
      alert("Please enter a username");
      return;
    }

    try {
      const response = await fetch(`${API_BASE_URL}/api/session/join`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, isAdmin: true }),
      });

      const data = await response.json();
      setUserId(data.userId);
      setIsAdmin(data.isAdmin);
      setHasJoined(true);
      setSessionId(data.sessionId);

      localStorage.setItem(
        "planningPokerUser",
        JSON.stringify({
          userId: data.userId,
          username,
          isAdmin: data.isAdmin,
          sessionId: data.sessionId,
        })
      );
    } catch (err) {
      console.error("Create failed:", err);
      alert("Failed to create session.");
    }
  };

  const selectCard = async (value) => {
    if (!showResults) {
      setSelectedCard(value);
      await fetch(`${API_BASE_URL}/api/session/vote`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userId, vote: value }),
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
        method: "POST",
      });

      if (!response.ok) throw new Error("Failed to reset votes on server");

      console.log("Votes cleared successfully.");
    } catch (err) {
      console.error("Reset failed:", err);
      alert("Failed to reset votes on server.");
    }
  };

  const logout = () => {
    localStorage.removeItem("planningPokerUser");
    setUserId(null);
    setUsername("");
    setIsAdmin(false);
    setHasJoined(false);
    setSelectedCard(null);
    setVotes([]);
    setAverage(null);
    setShowResults(false);
    setSessionId(null);
  };

  // Show only create or join depending on URL param
  if (!hasJoined) {
    const isJoining = !!sessionId;

    return (
      <div className="flex flex-col items-center justify-center min-h-screen p-6 space-y-4">
        <h1 className="text-3xl font-bold">Planning Poker</h1>
        <input
          type="text"
          placeholder="Enter your username"
          className="border rounded p-2 w-64"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <button
          onClick={isJoining ? handleJoin : handleCreate}
          className={`${
            isJoining ? "bg-green-500 hover:bg-green-700" : "bg-blue-500 hover:bg-blue-700"
          } text-white font-bold py-2 px-4 rounded`}
        >
          {isJoining ? "Join Game" : "Create Game"}
        </button>
      </div>
    );
  }

  return (
    <div className="flex flex-col items-center p-6 space-y-6">
      <h1 className="text-2xl font-bold">Planning Poker</h1>
      <p className="text-sm text-gray-600">Welcome, {username}</p>

      {isAdmin && sessionId && (
        <div className="mt-4 text-sm text-gray-700">
          <p>Invite others with this link:</p>
          <code className="bg-gray-100 p-2 rounded block">
            {`${window.location.origin}?sessionId=${sessionId}`}
          </code>
        </div>
      )}

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

      {isAdmin && (
        <button
          onClick={revealCards}
          className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
        >
          Reveal Cards
        </button>
      )}

      {showResults && isAdmin && (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="text-xl font-bold mt-4"
        >
          <h2>Votes and Average:</h2>
          <ul className="space-y-2">
            {Array.isArray(votes) &&
              votes.map(({ username, vote }) => (
                <li key={username} className="text-lg">
                  {username}: {vote}
                </li>
              ))}
          </ul>
          <p className="font-bold mt-4">Average Vote: {average}</p>
        </motion.div>
      )}

      <div className="flex gap-4 mt-4">
        <button
          onClick={resetGame}
          className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
        >
          Reset
        </button>
        <button
          onClick={logout}
          className="bg-gray-400 hover:bg-gray-600 text-white font-bold py-2 px-4 rounded"
        >
          Logout
        </button>
      </div>
    </div>
  );
}
