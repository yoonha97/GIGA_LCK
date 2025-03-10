import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/analysis';

// 선수 분석 정보 가져오기
export const getPlayerAnalysis = async (gameName, tagLine) => {
  console.log(`Fetching analysis for ${gameName}#${tagLine}`);
  try {
    const url = `${API_BASE_URL}/player/${gameName}`;
    console.log('API request URL:', url);
    console.log('API request params:', { tagLine });

    const response = await axios.get(url, {
      params: { tagLine },
    });

    console.log('Analysis API response:', response.data);
    return response.data;
  } catch (error) {
    console.error('선수 분석 정보를 불러오는 중 오류 발생:', error);
    if (error.response) {
      console.error('Error response:', error.response.data);
      console.error('Error status:', error.response.status);
      console.error('Error headers:', error.response.headers);
    } else if (error.request) {
      console.error('Error request - no response received:', error.request);
    } else {
      console.error('Error message:', error.message);
    }
    return null;
  }
};
