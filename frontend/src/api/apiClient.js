export async function parseResponse(response) {
  const json = await response.json();

  if (!response.ok) {
    throw new Error(json.message || "エラーが発生しました。");
  }

  return json;
}