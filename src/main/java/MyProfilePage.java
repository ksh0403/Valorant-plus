import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.RingPlot;
import org.jfree.data.general.DefaultPieDataset;
import org.openqa.selenium.NoSuchElementException;
import java.util.List;

public class MyProfilePage extends JPanel {
    private static User user; // User 클래스의 인스턴스 추가
    public MyProfilePage(User user) {
        this.user = user;

        // 전체 패널
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 간격 조절
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(1500, 1000));

        // 이미지 패널
        JPanel imagePanel = new JPanel();
        imagePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 0, 10)); // 간격 조절
        imagePanel.setOpaque(false); // 배경 투명화

        // 프로필 이미지
        ImageIcon imageIcon = new ImageIcon(Menu.class.getResource("/images/profile_img.png"));
        // 이미지 크기 조절 (예: 100x100 픽셀)
        Image scaledImage = imageIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(imageIcon);

        // 이미지 패널에 이미지 레이블 추가
        imagePanel.add(imageLabel);

        // 플레이어 정보 패널
        JPanel playerInfoPanel = new JPanel();
        playerInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 100, 10)); // 간격 조절
        playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));
        playerInfoPanel.setOpaque(false); // 배경 투명화

        // 최근 20게임 패널
        JPanel recent20Panel = new JPanel();
        recent20Panel.setLayout(new BoxLayout(recent20Panel, BoxLayout.Y_AXIS));
        recent20Panel.setBackground(Color.WHITE);
        recent20Panel.setPreferredSize(new Dimension(300, 400));
        recent20Panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 150, 0));

        // 최근 KDA Info 패널
        JPanel recentKdaInfoPanel = new JPanel();
        recentKdaInfoPanel.setLayout(new BoxLayout(recentKdaInfoPanel, BoxLayout.Y_AXIS));
        recentKdaInfoPanel.setBackground(Color.WHITE);
        recentKdaInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 270, 0)); // 간격 조절

        // rightPanel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 270, 20)); // 간격 조절

        // User 클래스의 인코딩 URL
        String url = user.getEncodedUrl();

        try {
            // Jsoup을 사용하여 웹 페이지 가져오기
            Document doc = Jsoup.connect(url).get();

            // 플레이어 이름
            Elements playerNameElements = doc.select("div.player-name");
            for (Element element : playerNameElements) {
                String data = element.text();
                // 플레이어 이름 레이블
                JLabel playerNameLabel = new JLabel(data);
                playerNameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 35));
                playerNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 왼쪽 정렬
                playerInfoPanel.add(playerNameLabel);
            }

            // ConfigReader 클래스를 참조해 ChromeDriver 경로 설정
            System.setProperty("webdriver.chrome.driver", ConfigReader.getWebDriverPath());

            // Headless 모드 설정
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");

            // WebDriver 초기화
            WebDriver driver = new ChromeDriver();
            driver.get(url);

            // 플레이어 티어
            WebElement tierElement = driver.findElement(By.cssSelector("div.tier-rank-info strong.tier-rank"));
            if (tierElement != null) {
                String tier = tierElement.getText();
                System.out.println("플레이어 티어 정보: " + tier);
                // 플레이어 티어 레이블
                JLabel playerTierLabel = new JLabel(tier);
                playerTierLabel.setFont(new Font("맑은 고딕", Font.BOLD, 30));
                playerTierLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 왼쪽 정렬
                playerInfoPanel.add(playerTierLabel);
            } else {
                System.out.println("티어 정보를 찾을 수 없습니다.");
            }

            //
            // leftPanel = recent20Panel + recentKdaInfoPanel
            // recent20Panel 내부 요소
            // 최근 20게임
            WebElement recent20Element = driver.findElement(By.cssSelector("div.recent-stats-head"));
            if (recent20Element != null) {
                String recent20 = recent20Element.getText();
                System.out.println("최근 20게임 정보: " + recent20);
                // 최근 20게임 레이블
                JLabel recent20Label = new JLabel(recent20);
                recent20Label.setFont(new Font("맑은 고딕", Font.BOLD, 25));
                recent20Label.setAlignmentX(Component.CENTER_ALIGNMENT); // 가운데 정렬
                recent20Panel.add(recent20Label);
            } else {
                System.out.println("최근 20게임 정보를 찾을 수 없습니다.");
            }

            // 최근 20게임 승률
            WebElement winningRateElement = driver.findElement(By.cssSelector("div.chart__text"));
            if (winningRateElement != null) {
                String winningRate = winningRateElement.getText();
                System.out.println("최근 20게임 승률: " + winningRate);

                // 승률 데이터를 추출하고 데이터셋에 추가
                double winRate = Double.parseDouble(winningRate.replaceAll("[^0-9.]", ""));
                DefaultPieDataset dataset = new DefaultPieDataset();
                dataset.setValue("Win", winRate);
                dataset.setValue("Loss", 100 - winRate);

                JFreeChart chart = ChartFactory.createRingChart(
                        "",  // 차트 제목
                        dataset,  // 데이터셋
                        true,  // 범례 표시 여부
                        true,
                        false
                );

                RingPlot plot = (RingPlot) chart.getPlot();
                plot.setSectionDepth(0.33); // 원형 차트 섹션 깊이
                plot.setBackgroundPaint(new Color(0, 0, 0, 0)); // 배경색을 투명으로 설정
                plot.setLabelGenerator(null); // 웨지 꼬리표(label) 제거
                plot.setOutlineVisible(false); // 외곽선 비활성화
                chart.getLegend().setVisible(false); // 레전드 비활성화
                plot.setShadowPaint(null); // 그림자 효과 비활성화

                // 차트를 패널에 추가
                ChartPanel chartPanel = new ChartPanel(chart);
                int chartSize = 300;
                chartPanel.setPreferredSize(new Dimension(chartSize, chartSize));
                chartPanel.setMaximumSize(new Dimension(chartSize, chartSize));
                chartPanel.setMinimumSize(new Dimension(chartSize, chartSize));

                chartPanel.setBackground(new Color(0, 0, 0, 0)); // 배경색을 투명으로 설정
                chartPanel.setOpaque(false); // 패널을 투명하게 설정
                recent20Panel.add(chartPanel);

                // 최근 20게임 승률 레이블
                JLabel winningRateLabel = new JLabel(winningRate);
                winningRateLabel.setFont(new Font("맑은 고딕", Font.BOLD, 25));
                winningRateLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 가운데 정렬
                winningRateLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // 간격 조절
                recent20Panel.add(winningRateLabel);
            } else {
                System.out.println("최근 20게임 승률을 찾을 수 없습니다.");
            }

            //
            // recentKdaInfoPanel 내부 요소
            // 최근 KDA
            WebElement recentKdaElement = driver.findElement(By.cssSelector("div.css-x43pu8"));
            if (recentKdaElement != null) {
                String recentKda = recentKdaElement.getText();
                System.out.println("최근 20게임 KDA: " + recentKda);
                // 최근 KDA 레이블
                JLabel recentKdaLabel = new JLabel(recentKda);
                recentKdaLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
                recentKdaLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 왼쪽 정렬
                recentKdaInfoPanel.add(recentKdaLabel);
            } else {
                System.out.println("최근 20게임 KDA를 찾을 수 없습니다.");
            }

            // 최근 KDA-ratio
            WebElement recentKdaRatioElement = driver.findElement(By.cssSelector("div.kda-ratio"));
            if (recentKdaRatioElement != null) {
                String recentKdaRatio = recentKdaRatioElement.getText();
                System.out.println("최근 20게임 KDA-ratio: " + recentKdaRatio);
                // 최근 KDA-ratio 레이블
                JLabel recentKdaRatioLabel = new JLabel(recentKdaRatio);
                recentKdaRatioLabel.setFont(new Font("맑은 고딕", Font.BOLD, 35));
                recentKdaRatioLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 왼쪽 정렬
                recentKdaInfoPanel.add(recentKdaRatioLabel);
            } else {
                System.out.println("최근 20게임 KDA-ratio를 찾을 수 없습니다.");
            }

            // 최근 킬관여율
            WebElement recentPerKillElement = driver.findElement(By.cssSelector("div.p-kill"));
            if (recentPerKillElement != null) {
                String recentPerKill = recentPerKillElement.getText();
                System.out.println("최근 20게임 킬관여율: " + recentPerKill);
                // 최근 KDA 레이블
                JLabel recentPerKillLabel = new JLabel(recentPerKill);
                recentPerKillLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
                recentPerKillLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 왼쪽 정렬
                recentKdaInfoPanel.add(recentPerKillLabel);
            } else {
                System.out.println("최근 20게임 킬관여율을 찾을 수 없습니다.");
            }

            //
            // rightPanel
            // 최근 플레이한 요원
            JLabel agentTextLable = new JLabel(" 최근 플레이한 요원 ");
            agentTextLable.setFont(new Font("맑은 고딕", Font.BOLD, 20));
            agentTextLable.setAlignmentX(Component.CENTER_ALIGNMENT); // 가운데 정렬
            agentTextLable.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 테두리 추가
            rightPanel.add(agentTextLable);

            // 여백 추가
            rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));

            // 패널 배열 생성
            JPanel[] recentPlayAgentPanels = new JPanel[10];

            // recentPlayAgentXPanel 생성
            JPanel recentPlayAgentXPanel = new JPanel();
            recentPlayAgentXPanel.setLayout(new BoxLayout(recentPlayAgentXPanel, BoxLayout.X_AXIS));
            recentPlayAgentXPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // 가운데 정렬
            recentPlayAgentXPanel.setBackground(Color.WHITE);
            recentPlayAgentXPanel.setBorder(BorderFactory.createEmptyBorder(0, 250, 0, 250));

            // 3개의 최근 플레이한 요원 정보를 recentPlayAgentXPanel에 추가
            for (int i = 0; i < 3; i++) {
                recentPlayAgentPanels[i] = new JPanel();
                recentPlayAgentPanels[i].setPreferredSize(new Dimension(100, 100));
                recentPlayAgentPanels[i].setAlignmentX(Component.CENTER_ALIGNMENT); // 가운데 정렬
                recentPlayAgentPanels[i].setBackground(Color.WHITE);

                List<WebElement> agentContainers = driver.findElements(By.cssSelector(".css-1biev8v img"));
                WebElement agentElement = agentContainers.get(i);

                if (agentElement != null) {
                    String agent = agentElement.getAttribute("alt");
                    System.out.println("최근 플레이한 요원" + (i+1) + " : " + agent);

                    // 에이전트에 대한 이미지 가져오기
                    ImageIcon agentImage = getAgentImage(agent);

                    if (agentImage != null) {
                        Image resizedImage = agentImage.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        ImageIcon resizedAgentImage = new ImageIcon(resizedImage);

                        JLabel agentImageLabel = new JLabel(resizedAgentImage);
                        recentPlayAgentPanels[i].add(agentImageLabel);

                        // recentPlayAgentXPanel에 패널 배열 추가
                        recentPlayAgentXPanel.add(recentPlayAgentPanels[i]);

                        // 여백 추가
                        recentPlayAgentXPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                    } else {
                        // 이미지가 null인 경우
                        System.out.println("이미지가 null 입니다.");
                    }

                } else {
                    System.out.println("최근 플레이한 요원을 찾을 수 없습니다.");
                }
            }

            // rightPanel에 recentPlayAgentXPanel 추가
            rightPanel.add(recentPlayAgentXPanel);

            // 여백 추가
            rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));

            // 최근 3 게임 레이블
            JLabel tempLable = new JLabel(" 최근 3 게임 ");
            tempLable.setFont(new Font("맑은 고딕", Font.BOLD, 20));
            tempLable.setAlignmentX(Component.CENTER_ALIGNMENT); // 가운데 정렬
            tempLable.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 테두리 추가
            rightPanel.add(tempLable);

            // 여백 추가
            rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            // recentGamePanels (rightPanel 내부 요소)
            // 패널 배열 생성
            JPanel[] recentGamePanels = new JPanel[10];

            // 3개의 최근 게임 정보 표시
            for (int i = 0; i < 3; i++) {
                recentGamePanels[i] = new JPanel();

                // recentGamePanels 하나의 크기 설정
                recentGamePanels[i].setPreferredSize(new Dimension(630, 100));
                recentGamePanels[i].setMaximumSize(new Dimension(630, 100));
                recentGamePanels[i].setMinimumSize(new Dimension(630, 100));

                try {
                    // 최근 게임 타입
                    List<WebElement> gameTypeContainers = driver.findElements(By.cssSelector("div.queue-type"));
                    WebElement gameTypeElement = gameTypeContainers.get(i);

                    String gameType = gameTypeElement.getText();
                    System.out.println("최근 gameType: " + gameType);
                    JLabel gameTypeLabel = new JLabel(gameType);
                    gameTypeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
                    gameTypeLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 세로로 가운데 정렬
                    gameTypeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 15)); // 간격 조절
                    gameTypeLabel.setForeground(Color.WHITE);

                    // 최근 게임 결과
                    List<WebElement> gameResultContainers = driver.findElements(By.cssSelector("div.result div"));
                    WebElement gameResultElement = gameResultContainers.get(i);

                    String gameResult = gameResultElement.getText();
                    System.out.println("최근 gameResult: " + gameResult);
                    JLabel gameResultLabel = new JLabel(gameResult);
                    gameResultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 32));
                    gameResultLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 세로로 가운데 정렬
                    gameResultLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 15)); // 간격 조절
                    gameResultLabel.setForeground(Color.WHITE);

                    // 각 게임 KDA
                    List<WebElement> eachKDAContainers = driver.findElements(By.cssSelector("div.css-1o04r3"));
                    WebElement eachKDAElement = eachKDAContainers.get(i);

                    String eachKDA = eachKDAElement.getText();
                    System.out.println("각 게임의 KDA: " + eachKDA);
                    JLabel eachKDALabel = new JLabel(eachKDA);
                    eachKDALabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
                    eachKDALabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 세로로 가운데 정렬
                    eachKDALabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 15)); // 간격 조절
                    eachKDALabel.setForeground(Color.WHITE);

                    // 각 게임 맵
                    List<WebElement> eachMapContainers = driver.findElements(By.cssSelector("div.map-name"));
                    WebElement eachMapElement = eachMapContainers.get(i);

                    String eachMap = eachMapElement.getText();
                    System.out.println("각 게임의 맵: " + eachMap);
                    JLabel eachMapLabel = new JLabel(eachMap);
                    eachMapLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
                    eachMapLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 세로로 가운데 정렬
                    eachMapLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 15)); // 간격 조절
                    eachMapLabel.setForeground(Color.WHITE);

                    // 각 게임 스코어
                    List<WebElement> eachGameScoreContainers = driver.findElements(By.cssSelector("div.game-score"));
                    WebElement eachGameScoreElement = eachGameScoreContainers.get(i);

                    WebElement firstScoreElement = eachGameScoreElement.findElement(By.xpath("span[1]"));
                    WebElement secondScoreElement = eachGameScoreElement.findElement(By.xpath("span[2]"));
                    String ourScore = firstScoreElement.getText();
                    String otherScore = secondScoreElement.getText();

                    String eachGameScore = ourScore + " : " + otherScore;

                    System.out.println("각 게임의 스코어: " + eachGameScore);
                    JLabel eachGameScoreLabel = new JLabel(eachGameScore);
                    eachGameScoreLabel.setFont(new Font("맑은 고딕", Font.BOLD, 32));
                    eachGameScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 세로로 가운데 정렬
                    eachGameScoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 15)); // 간격 조절
                    eachGameScoreLabel.setForeground(Color.WHITE);

                    // 헤드샷 레이블
                    JLabel headshotTextLable = new JLabel("헤드샷");
                    headshotTextLable.setFont(new Font("맑은 고딕", Font.BOLD, 15));
                    headshotTextLable.setAlignmentX(Component.CENTER_ALIGNMENT); // 가운데 정렬
                    headshotTextLable.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20)); // 간격 조절
                    headshotTextLable.setOpaque(false);  // 배경 투명 설정
                    headshotTextLable.setForeground(Color.WHITE);

                    // 각 게임 헤드샷 적중률
                    List<WebElement> headshotRateContainers = driver.findElements(By.xpath("//div[@class='match-stat']/div[contains(span, '헤드샷')]"));
                    WebElement headshotRateElement = headshotRateContainers.get(i);

                    String headshotRateText = headshotRateElement.getText();
                    String headshotRate = headshotRateText.replaceAll("[^\\d.]+", ""); // 숫자와 소수점만 남기기
                    System.out.println("헤드샷 적중률: " + headshotRate + "%");
                    JLabel headshotRateLabel = new JLabel(headshotRate + "%");
                    headshotRateLabel.setFont(new Font("맑은 고딕", Font.BOLD, 32));
                    headshotRateLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 세로로 가운데 정렬
                    headshotRateLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20)); // 간격 조절
                    headshotRateLabel.setOpaque(false);  // 배경 투명 설정
                    headshotRateLabel.setForeground(Color.WHITE);

                    // headshotPanel = headshotTextLable + headshotRateLabel
                    JPanel headshotPanel = new JPanel();
                    headshotPanel.setLayout(new BoxLayout(headshotPanel, BoxLayout.Y_AXIS));
                    headshotPanel.add(headshotTextLable);
                    headshotPanel.add(headshotRateLabel);
                    headshotPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // 세로로 가운데 정렬
                    headshotPanel.setOpaque(false);  // 배경 투명 설정
                    headshotPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 40)); // 간격 조절

                    // gameTRPanel = gameTypeLabel + gameResultLabel
                    JPanel gameTRPanel = new JPanel();
                    gameTRPanel.setLayout(new BoxLayout(gameTRPanel, BoxLayout.Y_AXIS));
                    gameTRPanel.add(gameTypeLabel);
                    gameTRPanel.add(gameResultLabel);
                    gameTRPanel.setOpaque(false);  // 배경 투명 설정
                    gameTRPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 20)); // 간격 조절

                    // gameMSPanel = eachMapLabel + eachGameScoreLabel
                    JPanel gameMSPanel = new JPanel();
                    gameMSPanel.setLayout(new BoxLayout(gameMSPanel, BoxLayout.Y_AXIS));
                    gameMSPanel.add(eachMapLabel);
                    gameMSPanel.add(eachGameScoreLabel);
                    gameMSPanel.setOpaque(false);  // 배경 투명 설정
                    gameMSPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 간격 조절

                    // recentGamePanel 생성
                    recentGamePanels[i].setLayout(new BoxLayout(recentGamePanels[i], BoxLayout.X_AXIS));
                    // gameTRPanel과 eachKDALabel을 recentGamePanel에 추가
                    recentGamePanels[i].add(gameTRPanel);
                    recentGamePanels[i].add(eachKDALabel);
                    recentGamePanels[i].add(gameMSPanel);
                    recentGamePanels[i].add(headshotPanel);

                    // gameResult에 따라 배경색 설정
                    if ("승리".equals(gameResult)) {
                        recentGamePanels[i].setBackground(new Color(0x5383E8)); // 승리일 경우
                    } else if ("패배".equals(gameResult)) {
                        recentGamePanels[i].setBackground(new Color(0xE84057)); // 패배일 경우
                    }

                    // rightPanel에 패널 배열 추가
                    rightPanel.add(recentGamePanels[i]);

                    // 여백 추가
                    rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));

                } catch (NoSuchElementException e) {
                    System.out.println("최근 게임 정보를 찾을 수 없습니다.");
                }
            }

            // WebDriver 종료
            driver.quit();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // profilePanel : imagePanel, playerInfoPanel 묶음
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BorderLayout());
        profilePanel.add(imagePanel, BorderLayout.WEST);
        profilePanel.add(playerInfoPanel, BorderLayout.CENTER);
        profilePanel.setOpaque(false); // 배경 투명화

        profilePanel.setPreferredSize(new Dimension(600, 200));
        profilePanel.setMaximumSize(new Dimension(600, 200));
        profilePanel.setMinimumSize(new Dimension(600, 200));

        // profilePanel에 둥근 테두리 추가
        profilePanel.setBorder(new RoundBorder(20)); // 반지름 크기 조절 가능

        // mainPanel 여백 추가
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));  // 여백 설정
        mainPanel.add(profilePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50))); // 여백 추가

        // leftPanel : recent20Panel, recentKdaInfoPanel 묶음
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        leftPanel.add(recent20Panel);
        leftPanel.add(recentKdaInfoPanel);
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(600, 800));
        leftPanel.setMaximumSize(new Dimension(600, 800));
        leftPanel.setMinimumSize(new Dimension(600, 800));

        // bottomPanel : leftPanel, rightPanel 묶음
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        // 두 패널의 폭을 동일한 비율로 조절
        double leftPanelWidth = leftPanel.getPreferredSize().getWidth();
        double rightPanelWidth = rightPanel.getPreferredSize().getWidth();
        double preferredWidth = Math.max(leftPanelWidth, rightPanelWidth);
        leftPanel.setPreferredSize(new Dimension((int) preferredWidth, leftPanel.getPreferredSize().height));
        rightPanel.setPreferredSize(new Dimension((int) preferredWidth, rightPanel.getPreferredSize().height));

        bottomPanel.add(leftPanel);
        bottomPanel.add(rightPanel);
        bottomPanel.setBackground(Color.WHITE);

        // 메인패널에 bottomPanel 추가
        mainPanel.add(bottomPanel);

        // mainPanel 추가
        add(mainPanel);

    }


    // 요원 이미지를 가져오는 함수
    public static ImageIcon getAgentImage(String agent) {
        // agent 값을 소문자로 변환
        String lowercaseAgent = agent.toLowerCase();

        // 각 요원에 대한 이미지 경로 목록
        String[] agentImagePaths = {
                "images/agent1.png", "images/agent2.png", "images/agent3.png", "images/agent4.png",
                "images/agent5.png", "images/agent6.png", "images/agent7.png", "images/agent8.png",
                "images/agent9.png", "images/agent10.png", "images/agent11.png", "images/agent12.png",
                "images/agent13.png", "images/agent14.png", "images/agent15.png", "images/agent16.png",
                "images/agent17.png", "images/agent18.png", "images/agent19.png", "images/agent20.png",
                "images/agent21.png", "images/agent22.png", "images/agent23.png"
        };

        // 주어진 agent와 일치하는 이미지 경로 찾기
        for (int i = 0; i < agentImagePaths.length; i++) {
            if (lowercaseAgent.equals(getAgentName(i + 1))) {
                return new ImageIcon(MyProfilePage.class.getResource(agentImagePaths[i]));
            }
        }

        // 일치하는 이미지가 없을 경우 null 반환
        return null;
    }

    // agent 번호에 따른 agent 이름을 반환하는 함수
    private static String getAgentName(int agentNumber) {
        switch (agentNumber) {
            case 1:
                return "jett";
            case 2:
                return "raze";
            case 3:
                return "breach";
            case 4:
                return "omen";
            case 5:
                return "brimstone";
            case 6:
                return "phoenix";
            case 7:
                return "sage";
            case 8:
                return "sova";
            case 9:
                return "viper";
            case 10:
                return "cypher";
            case 11:
                return "reyna";
            case 12:
                return "killjoy";
            case 13:
                return "skye";
            case 14:
                return "yoru";
            case 15:
                return "astra";
            case 16:
                return "kayo";
            case 17:
                return "chamber";
            case 18:
                return "neon";
            case 19:
                return "fade";
            case 20:
                return "harbor";
            case 21:
                return "gekko";
            case 22:
                return "deadlock";
            case 23:
                return "iso";
            default:
                return "";
        }
    }


}



