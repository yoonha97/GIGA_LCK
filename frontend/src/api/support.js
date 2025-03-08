import axios from 'axios';

// The base URL needs to match the controller's RequestMapping
const API_BASE_URL = 'http://localhost:8080/api/teams';

// 팀 이름을 ID로 변환하는 매핑 함수
// 백엔드가 숫자 ID를 기대하므로 이름을 적절한 ID로 변환
const getNumericTeamId = (teamId) => {
  // 이미 숫자면 그대로 반환
  if (!isNaN(teamId)) {
    return teamId;
  }

  // 팀 이름을 ID로 매핑 (백엔드와 일치해야 함)
  const teamMapping = {
    T1: 1,
    GEN: 2,
    DK: 3,
    BRO: 4,
    NS: 5,
    DRX: 6,
    HLE: 7,
    KT: 8,
    DNF: 9,
    BFX: 10,
  };

  // 매핑에 없는 경우 기본값 (백엔드와 상의 필요)
  return teamMapping[teamId] || 1;
};

// 팀별 응원 댓글 목록 조회
export const fetchSupportsByTeam = async (teamId) => {
  try {
    const numericId = getNumericTeamId(teamId);
    const response = await axios.get(`${API_BASE_URL}/${numericId}/supports`);
    return response.data;
  } catch (error) {
    console.error('응원 댓글을 불러오는 중 오류 발생:', error);
    return [];
  }
};

// 응원 댓글 상세 조회
export const fetchSupportById = async (teamId, supportId) => {
  try {
    const numericId = getNumericTeamId(teamId);
    const response = await axios.get(
      `${API_BASE_URL}/${numericId}/supports/${supportId}`
    );
    return response.data;
  } catch (error) {
    console.error('응원 댓글 상세 정보를 불러오는 중 오류 발생:', error);
    return null;
  }
};

// 응원 댓글 등록
export const submitSupport = async (teamId, supportComment) => {
  try {
    const numericId = getNumericTeamId(teamId);
    const response = await axios.post(`${API_BASE_URL}/${numericId}/supports`, {
      supportComment,
    });
    return response.data;
  } catch (error) {
    console.error('응원 댓글 등록 중 오류 발생:', error);
    throw error;
  }
};

// 응원 댓글 수정
export const updateSupport = async (teamId, supportId, supportComment) => {
  try {
    const numericId = getNumericTeamId(teamId);
    const response = await axios.put(
      `${API_BASE_URL}/${numericId}/supports/${supportId}`,
      {
        supportComment,
      }
    );
    return response.data;
  } catch (error) {
    console.error('응원 댓글 수정 중 오류 발생:', error);
    throw error;
  }
};

// 응원 댓글 삭제
export const deleteSupport = async (teamId, supportId) => {
  try {
    const numericId = getNumericTeamId(teamId);
    await axios.delete(`${API_BASE_URL}/${numericId}/supports/${supportId}`);
  } catch (error) {
    console.error('응원 댓글 삭제 중 오류 발생:', error);
    throw error;
  }
};
