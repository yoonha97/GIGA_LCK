import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/players';

// 모든 선수 목록 조회
export const fetchAllPlayers = async () => {
  try {
    const response = await axios.get(API_BASE_URL);
    return response.data;
  } catch (error) {
    console.error('선수 목록을 불러오는 중 오류 발생:', error);
    return [];
  }
};

// 선수 상세 정보 조회
export const fetchPlayerById = async (playerId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${playerId}`);
    return response.data;
  } catch (error) {
    console.error('선수 정보를 불러오는 중 오류 발생:', error);
    return null;
  }
};

// 포지션별 선수 목록 조회
export const fetchPlayersByPosition = async (position) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/position/${position}`);
    return response.data;
  } catch (error) {
    console.error(`${position} 포지션 선수를 불러오는 중 오류 발생:`, error);
    return [];
  }
};

// 팀별 선수 목록 조회
export const fetchPlayersByTeam = async (teamId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/team/${teamId}`);
    return response.data;
  } catch (error) {
    console.error('팀 선수 목록을 불러오는 중 오류 발생:', error);
    return [];
  }
};

// 새로운 선수 등록
export const createPlayer = async (playerData) => {
  try {
    console.log('Creating player with data:', playerData);

    // 백엔드에서 필요한 필드 이름으로 변환
    const payloadData = {
      playerName: playerData.playerName,
      position: playerData.position,
      description: playerData.description || null,
      teamId: playerData.teamId,
    };

    // 실제 이름이 존재하면 포함
    if (playerData.realName) {
      payloadData.realName = playerData.realName;
    }

    // 팀 ID가 문자열이고 null/undefined이 아니면 숫자로 변환
    if (payloadData.teamId !== null && payloadData.teamId !== undefined) {
      payloadData.teamId = Number(payloadData.teamId) || null;
    }

    const response = await axios.post(API_BASE_URL, payloadData);
    console.log('Create response:', response.data);
    return response.data;
  } catch (error) {
    console.error('선수 등록 중 오류 발생:', error);
    if (error.response) {
      console.error('Error response:', error.response.data);
      console.error('Error status:', error.response.status);
    }
    throw error;
  }
};

// 선수 정보 수정
export const updatePlayer = async (playerId, playerData) => {
  try {
    // 문자열이면 숫자로 변환
    playerId = Number(playerId) || playerId;

    console.log('Updating player with ID:', playerId, 'Data:', playerData);

    // 백엔드에서 필요한 필드 이름으로 변환
    const payloadData = {
      playerName: playerData.playerName,
      position: playerData.position,
      description: playerData.description || null,
      teamId: playerData.teamId,
    };

    // 실제 이름이 존재하면 포함
    if (playerData.realName) {
      payloadData.realName = playerData.realName;
    }

    // 팀 ID가 문자열이고 null/undefined이 아니면 숫자로 변환
    if (payloadData.teamId !== null && payloadData.teamId !== undefined) {
      payloadData.teamId = Number(payloadData.teamId) || null;
    }

    const response = await axios.put(
      `${API_BASE_URL}/${playerId}`,
      payloadData
    );
    console.log('Update response:', response.data);
    return response.data;
  } catch (error) {
    console.error('선수 정보 수정 중 오류 발생:', error);
    // 더 상세한 오류 정보 로깅
    if (error.response) {
      // 요청이 만들어졌고 서버가 상태 코드로 응답했음
      // 2xx 범위에 속하지 않는 경우
      console.error('Error response:', error.response.data);
      console.error('Error status:', error.response.status);
      console.error('Error headers:', error.response.headers);
    } else if (error.request) {
      // 요청이 만들어졌지만 응답이 수신되지 않음
      console.error('Error request:', error.request);
    } else {
      // 요청 설정 중에 발생한 오류
      console.error('Error message:', error.message);
    }
    throw error;
  }
};

// 선수 삭제
export const deletePlayer = async (playerId) => {
  try {
    // 문자열이면 숫자로 변환
    playerId = Number(playerId) || playerId;

    console.log('Deleting player with ID:', playerId);
    if (!playerId) {
      throw new Error('Player ID is required for deletion');
    }

    // 요청 URL 구성을 위해 문자열로 변환
    const response = await axios.delete(`${API_BASE_URL}/${playerId}`);
    console.log('Delete response:', response.status);
    return true;
  } catch (error) {
    console.error('선수 삭제 중 오류 발생:', error);
    // 더 상세한 오류 정보 로깅
    if (error.response) {
      console.error('Error response:', error.response.data);
      console.error('Error status:', error.response.status);
    } else if (error.request) {
      console.error('Error request:', error.request);
    } else {
      console.error('Error message:', error.message);
    }
    throw error;
  }
};

// 선수 상세 정보 가져오기 (이름으로)
export const getPlayerDetailsByName = async (playerName) => {
  try {
    // 이름으로 검색하는 API 엔드포인트가 없으므로 모든 선수를 가져와서 필터링
    const allPlayers = await fetchAllPlayers();
    return (
      allPlayers.find((player) => player.playerName === playerName) || null
    );
  } catch (error) {
    console.error('선수 정보 검색 중 오류 발생:', error);
    return null;
  }
};

// 추가 선수 정보 - 모달에 표시할 더 많은 정보
export const getDetailedPlayerInfo = async (playerName) => {
  console.log('선수 상세 정보 조회 시작:', playerName);

  // 먼저 API에서 선수 정보를 찾아보기
  const playerFromAPI = await getPlayerDetailsByName(playerName);
  console.log('API에서 찾은 선수 정보:', playerFromAPI);

  if (playerFromAPI) {
    // 포지션 정보가 제대로 매핑되었는지 확인
    if (!playerFromAPI.name && playerFromAPI.playerName) {
      playerFromAPI.name = playerFromAPI.playerName;
    }

    console.log('반환할 선수 정보:', playerFromAPI);
    return playerFromAPI;
  }

  // API에서 찾지 못한 경우 기본 정보만 반환
  console.log('API에서 선수 정보를 찾지 못함');
  return {
    name: playerName,
    playerName: playerName,
    position: 'Unknown',
  };
};
