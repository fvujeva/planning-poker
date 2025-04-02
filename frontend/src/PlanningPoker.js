import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import axios from "axios";

const PlanningPoker = () => {
  const [selectedVote, setSelectedVote] = useState(null);
  const [votes, setVotes] = useState([]);
  const voteOptions = [1, 2, 3, 5, 8, 13, 21];

  useEffect(() => {
    fetchVotes();
  }, []);

  const fetchVotes = async () => {
    try {
      const response = await axios.get("https://your-backend.com/api/votes");
      setVotes(response.data);
    } catch (error) {
      console.error("Error fetching votes:", error);
    }
  };

  const submitVote = async (vote) => {
    setSelectedVote(vote);
    try {
      await axios.post("https://your-backend.com/api/vote", { vote });
      fetchVotes();
    } catch (error) {
      console.error("Error submitting vote:", error);
    }
  };

  return (
    <div className="flex flex-col items-center p-8 min-h-screen bg-gray-50">
      <h1 className="text-3xl font-bold mb-6 animate-fade-in">Planning Poker</h1>

      {/* Voting Cards */}
      <div className="grid grid-cols-4 gap-6">
        {voteOptions.map((vote) => (
          <motion.div
            key={vote}
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            transition={{ duration: 0.2 }}
            onClick={() => submitVote(vote)}
          >
            <div
              className={`p-6 text-center cursor-pointer rounded-lg shadow-md transition-all 
                ${selectedVote === vote ? "bg-blue-500 text-white" : "bg-white hover:bg-blue-300"}`}
            >
              <span className="text-2xl font-semibold">{vote}</span>
            </div>
          </motion.div>
        ))}
      </div>

      {/* Vote Summary */}
      <div className="mt-6">
        <h2 className="text-lg font-semibold">Votes Summary:</h2>
        <ul className="mt-2 space-y-2">
          {votes.map((v, index) => (
            <motion.li
              key={index}
              className="text-gray-700"
              animate={{ opacity: 1 }}
              initial={{ opacity: 0 }}
              transition={{ duration: 0.5 }}
            >
              {`Vote: ${v.vote}`}
            </motion.li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default PlanningPoker;
