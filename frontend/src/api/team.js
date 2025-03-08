import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/teams';

// 모든 팀 정보 가져오기
export const fetchAllTeams = async () => {
  try {
    const response = await axios.get(API_BASE_URL);
    return response.data;
  } catch (error) {
    console.error('팀 목록을 불러오는 중 오류 발생:', error);
    return [];
  }
};

// ID로 팀 정보 가져오기
export const fetchTeamById = async (teamId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${teamId}`);
    return response.data;
  } catch (error) {
    console.error('팀 정보를 불러오는 중 오류 발생:', error);
    return null;
  }
};

// 팀 이름으로 팀 정보 가져오기
export const fetchTeamByName = async (teamName) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/by-name/${teamName}`);
    return response.data;
  } catch (error) {
    console.error('팀 정보를 불러오는 중 오류 발생:', error);
    return null;
  }
};
