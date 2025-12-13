import express from "express";
import cors from "cors";
import bodyParser from "body-parser";
import OpenAI from "openai";
import dotenv from "dotenv";
import mongoose from "mongoose";

dotenv.config();

// -------------------------------------
// MongoDB 연결
// -------------------------------------
mongoose
  .connect(process.env.MONGO_URI, { dbName: "questDB" })
  .then(() => console.log("✅ MongoDB Connected"))
  .catch((e) => console.error("❌ MongoDB Error:", e));

// -------------------------------------
// 스키마 & 모델
// -------------------------------------
const questSchema = new mongoose.Schema({
  userId: String,
  region: String,
  quests: Array,
});

const QuestModel = mongoose.model("Quest", questSchema);

// GPT
const client = new OpenAI({ apiKey: process.env.OPENAI_API_KEY });

// 지역 매핑
const regions = {
  junggu: "중구",
  seogu: "서구",
  yuseong: "유성구",
  daedeok: "대덕구",
  donggu: "동구",
};

const app = express();
app.use(cors());
app.use(bodyParser.json());

// -------------------------------------------------------
// 🔥 1. 모든 구에 대해 퀘스트 5개씩 자동 생성 API
// -------------------------------------------------------
app.post("/generateQuestAll", async (req, res) => {
  try {
    const userInfo = req.body;

    let results = {};

    for (const eng in regions) {
      const regionKo = regions[eng];

      const prompt = `
너는 여행 게임 퀘스트 마스터다.
대전 ${regionKo}에 대한 5개의 퀘스트를 JSON으로 출력해라.

조건:
- description은 1문장
- 실제 존재하는 장소만 사용
- id는 "${regionKo}-1" 같은 형태
- category 하나
- lat/lng 포함

사용자 정보:
${JSON.stringify(userInfo)}

JSON 형식:
{
  "quests": [
    {
      "id": "${regionKo}-1",
      "title": "제목",
      "description": "설명",
      "location": "장소",
      "category": "관광",
      "coordinates": { "lat": 36.32, "lng": 127.42 }
    }
  ]
}
`;

      const completion = await client.chat.completions.create({
        model: "gpt-4o",
        response_format: { type: "json_object" },
        messages: [
          { role: "system", content: "너는 JSON만 출력한다." },
          { role: "user", content: prompt },
        ],
      });

      const json = JSON.parse(completion.choices[0].message.content);

      // DB 저장
      await QuestModel.findOneAndUpdate(
        { userId: userInfo.userId, region: regionKo },
        { quests: json.quests },
        { upsert: true }
      );

      results[regionKo] = json.quests;
    }

    return res.json(results);
  } catch (e) {
    console.error(e);
    res.status(500).json({ error: e.message });
  }
});

// -------------------------------------------------------
// 🔥 2. 특정 구 퀘스트 불러오기 API
// -------------------------------------------------------
app.get("/quests", async (req, res) => {
  try {
    const region = req.query.region; // "중구"
    const userId = req.query.userId;

    const data = await QuestModel.findOne({ region, userId });

    if (!data) return res.status(404).json({ error: "퀘스트 없음" });

    res.json({ region, quests: data.quests });
  } catch (e) {
    res.status(500).json({ error: e.message });
  }
});

// 서버 실행
app.listen(3000, () => console.log("🚀 Server running on 3000"));
