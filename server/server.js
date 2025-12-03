import express from "express";
import cors from "cors";
import bodyParser from "body-parser";
import OpenAI from "openai";
import dotenv from "dotenv";

dotenv.config();

const app = express();
app.use(cors());
app.use(bodyParser.json());

const client = new OpenAI({
  apiKey: process.env.OPENAI_API_KEY
});

app.get("/", (req, res) => {
  res.send("Server is running!");
});

// ★ JSON 클린업 함수 (중요)
function extractJSON(text) {
  const start = text.indexOf("{");
  const end = text.lastIndexOf("}");
  if (start === -1 || end === -1) return null;
  const jsonString = text.substring(start, end + 1);
  try {
    return JSON.parse(jsonString);
  } catch (e) {
    return null;
  }
}

app.post("/generateQuest", async (req, res) => {
  try {
    const userInfo = req.body;

    const prompt = `
      아래 사용자 정보를 기반으로 퀘스트 2~3개를 생성해줘.
      반드시 JSON만 출력해야 하고 설명 금지.

      사용자 정보:
      ${JSON.stringify(userInfo)}

      JSON 예시:
      {
        "quests": [
          {
            "id": "q1",
            "title": "string",
            "description": "string",
            "location": "string",
            "duration": "string",
            "type": "string"
          }
        ]
      }
    `;

    const completion = await client.responses.create({
      model: "gpt-4o-mini",
      input: prompt
    });

    const text = completion.output_text.trim();

    // GPT 출력에서 JSON 추출 시도
    const json = extractJSON(text);

    if (!json) {
      return res.status(500).json({
        error: "Failed to parse JSON",
        original: text
      });
    }

    res.json(json);

  } catch (error) {
    console.error("❌ AI Error:", error);
    res.status(500).json({ error: error.message });
  }
});

app.listen(3000, () => {
  console.log("🚀 Server running on port 3000");
});
