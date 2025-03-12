-- 1. 팀 테이블 (Teams)
CREATE TABLE Teams (
    team_id SERIAL PRIMARY KEY,
	team_name VARCHAR(50) NOT NULL,
    owner VARCHAR(100),
    head_coach VARCHAR(100)
);

-- 2. 코치 테이블 (Coaches)
CREATE TABLE Coaches (
    coach_id SERIAL PRIMARY KEY,
    team_id INTEGER REFERENCES Teams(team_id) ON DELETE CASCADE,
    coach_name VARCHAR(100) NOT NULL
);

-- 3. 선수 테이블 (Players)
CREATE TABLE Players (
    player_id SERIAL PRIMARY KEY,
    team_id INTEGER REFERENCES Teams(team_id) ON DELETE SET NULL,
    player_name VARCHAR(100) NOT NULL,
    position VARCHAR(50),
    description TEXT
);

-- 4. 경기 일정 테이블 (Matches)
CREATE TABLE Matches (
    match_id SERIAL PRIMARY KEY,
    stage VARCHAR(100),
    home_team_id INTEGER NOT NULL REFERENCES Teams(team_id) ON DELETE CASCADE,
    away_team_id INTEGER NOT NULL REFERENCES Teams(team_id) ON DELETE CASCADE,
    date DATE,
    time VARCHAR(100),
    score VARCHAR(100)
);

-- 5. 리뷰 및 평점 테이블 (Reviews)
CREATE TABLE Reviews (
    review_id SERIAL PRIMARY KEY,
    match_id INTEGER REFERENCES Matches(match_id) ON DELETE CASCADE,
    rating INTEGER CHECK (rating BETWEEN 1 AND 5) NOT NULL, -- 평점 1~5 제한
    comment TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. 응원 댓글 테이블 (Supports)
CREATE TABLE Supports (
    support_id SERIAL PRIMARY KEY,
    team_id INTEGER REFERENCES Teams(team_id) ON DELETE CASCADE,
    support_comment TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. 라이엇 계정 테이블 (player_accounts)
CREATE TABLE player_accounts (
    account_id SERIAL PRIMARY KEY,
    player_id INTEGER NOT NULL REFERENCES Players(player_id) ON DELETE CASCADE,
    riot_id VARCHAR(100) NOT NULL,
	tag_line VARCHAR(100) NOT NULL
);


--------------------------------------------------------------------------------------


-- LCK 10개 팀 정보 INSERT
INSERT INTO Teams (team_id, team_name, owner, head_coach) VALUES
(1, 'BRO', 'Reach 박정석', 'Edgar 최우범'),
(2, 'DRX', 'BIGfafa 서민석', 'Ssong 김상수'),
(3, 'DNF', 'Sin Hyunsuk 신현석', 'RapidStar 정민성'),
(4, 'NS', 'Cha Minkyu 차민규', 'Chelly 박승진'),
(5, 'BFX', 'Harry 김해찬', 'Ryu 유상욱'),
(6, 'KT', 'Choi Hyunjoon 최현준', 'Score 고동빈'),
(7, 'T1', 'Becker 정회윤', 'kkOma 김정균'),
(8, 'DK', 'Kim Dongkyu 김동규', 'Bengi 배성웅'),
(9, 'HLE', 'Kim Seonghoon 김성훈', 'DanDy 최인규'),
(10, 'GEN', 'Arnold 아놀드 허', 'KIM 김정수');


-- 코치 정보 INSERT
INSERT INTO Coaches (team_id, coach_name) VALUES
(1, 'GuGer 김도엽'),
(2, 'Frozen 김태일'),
(3, 'FanTaSy 정명훈'),
(3, 'Punch 손민혁'),
(4, 'Crazy 김재희'),
(4, 'SSUN 김태양'),
(5, 'Joker 조재읍'),
(6, 'Museong 김무성'),
(6, 'Sonstar 손승익'),
(7, 'Tom 임재현'),
(7, 'Mata 조세형'),
(8, 'PoohManDu 이정현'),
(8, 'Hachani 하승찬'),
(9, 'Mowgli 이재하'),
(9, 'Shine 신동욱'),
(10, 'Helper 권영재'),
(10, 'Lyn 김다빈');




---------------------------------------------------------------------------------




-- BRO 팀 선수 추가
INSERT INTO Players (team_id, player_name, position, description) VALUES
(1, 'Morgan', 'Top', NULL),
(1, 'HamBak', 'Jungle', NULL),
(1, 'Clozer', 'Mid', NULL),
(1, 'Hype', 'Bot', NULL),
(1, 'Pollu', 'Support', NULL);

-- DRX 팀 선수 추가
INSERT INTO Players (team_id, player_name, position, description) VALUES
(2, 'Rich', 'Top', NULL),
(2, 'Sponge', 'Jungle', NULL),
(2, 'Ucal', 'Mid', NULL),
(2, 'Teddy', 'Bot', NULL),
(2, 'Andil', 'Support', NULL),
(2, 'Alive', 'Support', NULL);

-- DNF 팀 선수 추가
INSERT INTO Players (team_id, player_name, position, description) VALUES
(3, 'DuDu', 'Top', NULL),
(3, 'Pyosik', 'Jungle', NULL),
(3, 'BuLLDoG', 'Mid', NULL),
(3, 'Berserker', 'Bot', NULL),
(3, 'Life', 'Support', NULL);

-- NS 팀 선수 추가
INSERT INTO Players (team_id, player_name, position, description) VALUES
(4, 'Kingen', 'Top', NULL),
(4, 'GIDEON', 'Jungle', NULL),
(4, 'Fisher', 'Mid', NULL),
(4, 'Jiwoo', 'Bot', NULL),
(4, 'Lehends', 'Support', NULL);

-- BFX 팀 선수 추가
INSERT INTO Players (team_id, player_name, position, description) VALUES
(5, 'Clear', 'Top', NULL),
(5, 'Raptor', 'Jungle', NULL),
(5, 'VicLa', 'Mid', NULL),
(5, 'Diable', 'Bot', NULL),
(5, 'Kellin', 'Support', NULL);

-- KT 팀 선수 추가
INSERT INTO Players (team_id, player_name, position, description) VALUES
(6, 'PerfecT', 'Top', NULL),
(6, 'Cuzz', 'Jungle', NULL),
(6, 'Bdd', 'Mid', NULL),
(6, 'Deokdam', 'Bot', NULL),
(6, 'Way', 'Support', NULL);

-- T1 팀 선수 추가
INSERT INTO Players (team_id, player_name, position, description) VALUES
(7, 'Doran', 'Top', NULL),
(7, 'Oner', 'Jungle', NULL),
(7, 'Faker', 'Mid', NULL),
(7, 'Gumayusi', 'Bot', NULL),
(7, 'Smash', 'Bot', NULL),
(7, 'Keria', 'Support', NULL);

-- DK 팀 선수 추가
INSERT INTO Players (team_id, player_name, position, description) VALUES
(8, 'Siwoo', 'Top', NULL),
(8, 'Lucid', 'Jungle', NULL),
(8, 'ShowMaker', 'Mid', NULL),
(8, 'Aiming', 'Bot', NULL),
(8, 'BeryL', 'Support', NULL);

-- HLE 팀 선수 추가
INSERT INTO Players (team_id, player_name, position, description) VALUES
(9, 'Zeus', 'Top', NULL),
(9, 'Peanut', 'Jungle', NULL),
(9, 'Zeka', 'Mid', NULL),
(9, 'Viper', 'Bot', NULL),
(9, 'Delight', 'Support', NULL);

-- GEN 팀 선수 추가
INSERT INTO Players (team_id, player_name, position, description) VALUES
(10, 'Kiin', 'Top', NULL),
(10, 'Canyon', 'Jungle', NULL),
(10, 'Chovy', 'Mid', NULL),
(10, 'Ruler', 'Bot', NULL),
(10, 'Duro', 'Support', NULL);


-- 선수의 라이엇계정 추가
INSERT INTO player_accounts (player_id, riot_id, tag_line) VALUES
(27, 'diuepoti', 'qesdf'),
(28, 'Cuzz', 'KR1'),
(29, '아구몬', '0509'),
(30, '스컬지', 'K T'),
(31, '나는먼지', 'KR11');


---------------------------------------------------------------------------------


-- 1월 경기 일정
INSERT INTO Matches (stage, home_team_id, away_team_id, date, time, score) VALUES
('그룹 스테이지', 1, 2, '2025-01-15', '17:00', '1-2'),
('그룹 스테이지', 3, 4, '2025-01-15', '19:30', '2-1'),

('그룹 스테이지', 5, 6, '2025-01-16', '17:00', '1-2'),
('그룹 스테이지', 7, 8, '2025-01-16', '19:30', '1-2'),

('그룹 스테이지', 9, 10, '2025-01-17', '17:00', '0-2'),
('그룹 스테이지', 1, 4, '2025-01-17', '19:30', '0-2'),

('그룹 스테이지', 3, 8, '2025-01-18', '15:00', '0-2'),
('그룹 스테이지', 7, 2, '2025-01-18', '17:30', '2-0'),

('그룹 스테이지', 9, 6, '2025-01-19', '15:00', '2-0'),
('그룹 스테이지', 5, 10, '2025-01-19', '17:30', '0-2'),

('그룹 스테이지', 5, 8, '2025-01-22', '17:00', '1-2'),
('그룹 스테이지', 3, 2, '2025-01-22', '19:30', '1-2'),

('그룹 스테이지', 1, 10, '2025-01-23', '17:00', '2-0'),
('그룹 스테이지', 9, 4, '2025-01-23', '19:30', '2-0'),

('그룹 스테이지', 5, 2, '2025-01-24', '17:00', '1-2'),
('그룹 스테이지', 7, 8, '2025-01-24', '19:30', '1-2'),

('그룹 스테이지', 9, 8, '2025-01-25', '15:00', '1-2'),
('그룹 스테이지', 3, 10, '2025-01-25', '17:30', '0-2'),

('그룹 스테이지', 7, 4, '2025-01-26', '15:00', '2-0'),
('그룹 스테이지', 1, 6, '2025-01-26', '17:30', '0-2'),

('그룹 스테이지', 3, 6, '2025-01-31', '17:00', '1-2'),
('그룹 스테이지', 9, 2, '2025-01-31', '19:30', '2-0');

-- 2월 경기 일정
INSERT INTO Matches (stage, home_team_id, away_team_id, date, time, score) VALUES
('그룹 스테이지', 7, 10, '2025-02-01', '15:00', '2-1'),
('그룹 스테이지', 1, 8, '2025-02-01', '17:30', '1-2'),
('그룹 스테이지', 5, 4, '2025-02-02', '15:00', '0-2'),

('플레이오프 1R', 2, 3, '2025-02-07', '17:00', '2-0'),
('플레이오프 1R', 4, 1, '2025-02-07', '19:30', '2-0'),

('플레이오프 2R', 7, 4, '2025-02-08', '15:00', '2-0'),
('플레이오프 2R', 9, 2, '2025-02-08', '17:30', '2-0'),

('플레이오프 3R', 4, 2, '2025-02-09', '15:00', '3-1'),

('플레이오프 1R', 6, 4, '2025-02-12', '17:00', '0-3'),
('플레이오프 1R', 7, 9, '2025-02-13', '17:00', '2-3'),

('플레이오프 2R', 8, 4, '2025-02-15', '15:00', '3-1'),
('플레이오프 2R', 10, 9, '2025-02-16', '15:00', '2-3'),

('플레이오프 3R', 8, 9, '2025-02-19', '17:00', '2-3'),
('플레이오프 3R', 4, 10, '2025-02-20', '17:00', '2-3'),

('플레이오프 4R', 8, 10, '2025-02-22', '15:00', '0-3'),

('결승전', 9, 10, '2025-02-23', '15:00', '3-2');


-------------------------------------------------



SELECT * FROM matches;
SELECT * FROM teams;
SELECT * FROM reviews;
SELECT * FROM players;
SELECT * FROM player_accounts;