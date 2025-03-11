import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/analysis';

// 선수 분석 정보 가져오기
export const getPlayerAnalysis = async (gameName, tagLine) => {
  console.log(`Fetching analysis for ${gameName}#${tagLine}`);
  try {
    // URL에 포함되는 문자열은 인코딩
    const encodedGameName = encodeURIComponent(gameName);
    const encodedTagLine = encodeURIComponent(tagLine);

    const url = `${API_BASE_URL}/player/${encodedGameName}`;
    console.log('API request URL:', url);
    console.log('API request params:', { tagLine: encodedTagLine });

    // 요청 시간 제한 추가 (20초)
    const response = await axios.get(url, {
      params: { tagLine: encodedTagLine },
      timeout: 20000,
    });

    if (!response.data) {
      console.warn('API returned no data');
      return null;
    }

    console.log('Analysis API response:', response.data);
    return response.data;
  } catch (error) {
    console.error('선수 분석 정보를 불러오는 중 오류 발생:', error);
    if (error.response) {
      // 서버에서 응답이 왔지만 2xx 범위가 아닌 상태 코드
      console.error('Error response:', error.response.data);
      console.error('Error status:', error.response.status);
      console.error('Error headers:', error.response.headers);

      if (error.response.status === 404) {
        throw new Error('해당 선수의 분석 데이터를 찾을 수 없습니다.');
      } else if (error.response.status === 429) {
        throw new Error('요청이 너무 많습니다. 잠시 후 다시 시도해주세요.');
      } else if (error.response.status >= 500) {
        throw new Error('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
      }
    } else if (error.request) {
      // 요청은 보냈지만 응답을 받지 못함
      console.error('Error request - no response received:', error.request);
      throw new Error(
        '서버에 연결할 수 없습니다. 네트워크 연결을 확인해주세요.'
      );
    } else if (error.code === 'ECONNABORTED') {
      // 타임아웃 오류
      console.error('Request timeout:', error);
      throw new Error('요청 시간이 초과되었습니다. 나중에 다시 시도해주세요.');
    } else {
      // 요청 설정 중 오류 발생
      console.error('Error message:', error.message);
      throw new Error(`요청 중 오류가 발생했습니다: ${error.message}`);
    }

    // 위의 조건에 해당하지 않는 경우 기본 에러
    throw new Error('분석 정보를 불러오는 중 알 수 없는 오류가 발생했습니다.');
  }
};
