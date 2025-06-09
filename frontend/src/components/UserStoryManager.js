import React, { useState, useEffect, useCallback } from "react";
import TaskManager from "./TaskManager";

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

const UserStoryManager = ({ sessionId }) => {
  const [userStories, setUserStories] = useState([]);
  const [newTitle, setNewTitle] = useState("");
  const [newDescription, setNewDescription] = useState("");

  const fetchUserStories = useCallback(async () => {
    if (!sessionId) return;

    try {
      const res = await fetch(`${API_BASE_URL}/api/userstories/session/${sessionId}`);
      const data = await res.json();
      setUserStories(data);
    } catch (err) {
      console.error("Failed to fetch user stories:", err);
    }
  }, [sessionId]);

  useEffect(() => {
    fetchUserStories();
  }, [fetchUserStories]);

  const handleAddUserStory = async () => {
    if (!newTitle.trim() || !newDescription.trim()) {
      alert("Title and description are required.");
      return;
    }

    const dto = {
      title: newTitle,
      description: newDescription,
      sessionId: sessionId,
    };

    try {
      const response = await fetch(`${API_BASE_URL}/api/userstories`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(dto),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Failed to create user story.");
      }

      setNewTitle("");
      setNewDescription("");
      fetchUserStories();
    } catch (err) {
      console.error("Failed to add user story:", err);
      alert(err.message);
    }
  };

  const handleDeleteUserStory = async (storyId) => {
    try {
      await fetch(`${API_BASE_URL}/api/userstories/${storyId}`, {
        method: "DELETE",
      });
      fetchUserStories();
    } catch (err) {
      console.error("Failed to delete user story:", err);
    }
  };

  return (
    <div className="p-4 border rounded">
      <h2 className="text-xl font-bold mb-4">User Stories</h2>

      <ul className="space-y-4 mb-6">
        {userStories.map((story) => (
          <li key={story.id} className="border p-4 rounded">
            <div className="flex justify-between items-center mb-2">
              <div>
                <h3 className="text-lg font-semibold">{story.title}</h3>
                <p className="text-sm text-gray-600">{story.description}</p>
              </div>
              <button
                onClick={() => handleDeleteUserStory(story.id)}
                className="text-red-500 hover:text-red-700"
              >
                Delete
              </button>
            </div>
            <TaskManager userStoryId={story.id} />
          </li>
        ))}
      </ul>

      <div className="space-y-2">
        <input
          type="text"
          placeholder="User Story Title"
          value={newTitle}
          onChange={(e) => setNewTitle(e.target.value)}
          className="w-full p-2 border rounded"
        />
        <textarea
          placeholder="User Story Description"
          value={newDescription}
          onChange={(e) => setNewDescription(e.target.value)}
          className="w-full p-2 border rounded"
        />
        <button
          onClick={handleAddUserStory}
          className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded w-full"
        >
          Add User Story
        </button>
      </div>
    </div>
  );
};

export default UserStoryManager;
