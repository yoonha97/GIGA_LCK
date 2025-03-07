import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/matches';

export const fetchMatchesByMonth = async (year, month) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/month`, {
      params: { year, month },
    });
    return response.data;
  } catch (error) {
    console.error('경기 데이터를 불러오는 중 오류 발생:', error);
    return [];
  }
};

export const fetchMatchById = async (matchId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${matchId}`);
    return response.data;
  } catch (error) {
    console.error('경기 상세 데이터를 불러오는 중 오류 발생:', error);
    return null;
  }
};
