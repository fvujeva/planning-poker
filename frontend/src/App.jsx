import React, { useState, useEffect } from "react";
import CardComponent from "./components/ui/CardComponent";
import { motion } from "framer-motion";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import UserStoryManager from "./components/UserStoryManager";

const cardValues = [1, 2, 3, 5, 8, 13, 21, "?", "☕"];
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

export default function App() {
  const [username, setUsername] = useState("");
  const [acceptedTerms, setAcceptedTerms] = useState(false);
  const [hasJoined, setHasJoined] = useState(false);
  const [selectedCard, setSelectedCard] = useState(null);
  const [showResults, setShowResults] = useState(false);
  const [votes, setVotes] = useState([]);
  const [average, setAverage] = useState(null);
  const [userId, setUserId] = useState(null);
  const [isAdmin, setIsAdmin] = useState(false);
  const [sessionId, setSessionId] = useState(null);
  const [stompClient, setStompClient] = useState(null);
  const [sessionHistory, setSessionHistory] = useState([]);
  const [showHistory, setShowHistory] = useState(false);

  // Load session from localStorage
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

  // Connect to WebSocket when userId is available
  useEffect(() => {
    if (!userId) return;
  
    const socket = new SockJS(`${API_BASE_URL}/ws`);
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: () => {
        console.log("WebSocket connected");
  
        // Subscribe to results
        client.subscribe(`/topic/results/${sessionId}`, (message) => {
          try {
            const data = JSON.parse(message.body);
            setVotes(data.votes || []);
            setAverage(data.average || null);
            setShowResults(true);
          } catch (error) {
            console.error("Error parsing result message:", error);
          }
        });
  
        // Subscribe to reset
        client.subscribe(`/topic/reset/${sessionId}`, (message) => {
          try {
            const shouldReset = JSON.parse(message.body);
            if (shouldReset === true) {
              console.log("Reset signal received");
              setSelectedCard(null);
              setVotes([]);
              setAverage(null);
              setShowResults(false);
            }
          } catch (error) {
            console.error("Error parsing reset message:", error);
          }
        });
      },
      onStompError: (frame) => {
        console.error("STOMP Error:", frame);
      },
      onWebSocketError: (error) => {
        console.error("WebSocket Error:", error);
      },
    });
  
    client.activate();
    setStompClient(client);
  
    return () => {
      client.deactivate();
    };
  }, [userId, sessionId]);
  

  const handleJoin = async () => {
    if (!username) {
      alert("Please enter a username");
      return;
    }

    if (!acceptedTerms) {
      alert("You must accept the Terms and Conditions to continue.");
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

    // Notify backend to calculate and broadcast results via WebSocket
    await fetch(`${API_BASE_URL}/api/session/results/${userId}?sessionId=${sessionId}`);
  };

  const fetchSessionHistory = async () => {
    try {
      const res = await fetch(`${API_BASE_URL}/api/session/history/${userId}`);
      const data = await res.json();
      setSessionHistory(data);
      setShowHistory(true);
    } catch (error) {
      console.error("Failed to fetch session history:", error);
      alert("Could not load session history.");
    }
  };

  const resetGame = async () => {
    setSelectedCard(null);
    setShowResults(false);
    setVotes([]);
    setAverage(null);

    try {
      const response = await fetch(`${API_BASE_URL}/api/session/reset?sessionId=${sessionId}`, {
        method: "POST",
      });

      if (!response.ok) throw new Error("Failed to reset votes on server");
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

        <label className="flex items-start text-sm w-64 space-x-2">
          <input
              type="checkbox"
              checked={acceptedTerms}
              onChange={(e) => setAcceptedTerms(e.target.checked)}
              className="mt-1"
          />
          <span>
            I accept the{" "}
            <a href="/privacy-policy" target="_blank" className="text-blue-600 underline">
              Terms and Conditions
            </a>
          </span>
        </label>

        <button
          onClick={isJoining ? handleJoin : handleCreate}
          disabled={!acceptedTerms}
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

      {isAdmin && sessionId && (
        <UserStoryManager sessionId={sessionId} />
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

      {showResults && (
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
      {showHistory && (
          <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="mt-6 w-full max-w-2xl bg-white shadow p-4 rounded"
          >
            <h2 className="text-xl font-bold mb-2">Your Session History</h2>
            {sessionHistory.length === 0 ? (
                <p>No past sessions found.</p>
            ) : (
                sessionHistory.map((session, idx) => (
                    <div key={idx} className="mb-4 border-b pb-2">
                      <p className="font-semibold">Session ID: {session.sessionId}</p>
                      <p>Average Vote: {session.average}</p>
                      <ul className="ml-4 mt-1">
                        {session.votes.map((vote, i) => (
                            <li key={i}>
                              {vote.username}: {vote.value}
                            </li>
                        ))}
                      </ul>
                    </div>
                ))
            )}
            <button
                onClick={() => setShowHistory(false)}
                className="mt-4 bg-gray-300 hover:bg-gray-400 text-black font-semibold py-1 px-3 rounded"
            >
              Close
            </button>
          </motion.div>
      )}

      <div className="flex gap-4 mt-4">
      {isAdmin && (
        <button
            onClick={resetGame}
            className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
          >
            Reset
          </button>
        )}
        <button
            onClick={fetchSessionHistory}
            className="bg-indigo-500 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded"
        >
          View Session History
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
