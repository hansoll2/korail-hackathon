import mongoose from "mongoose";

const QuestSchema = new mongoose.Schema({
  region: { type: String, required: true },  
  id: { type: String, required: true },
  title: { type: String, required: true },
  description: { type: String, required: true },
  location: { type: String, required: true },
  category: { type: String, required: true },
  coordinates: {
    lat: Number,
    lng: Number
  }
});

export default mongoose.model("Quest", QuestSchema);
