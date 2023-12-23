import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsPage extends JPanel {
    private WebDriver driver;
    public NewsPage() {
        // 전체 패널
        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));  // 외부 여백 조정
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(1500, 1000));

        // ConfigReader 클래스를 참조해 ChromeDriver 경로 설정
        System.setProperty("webdriver.chrome.driver", ConfigReader.getWebDriverPath());

        // ChromeOptions 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--whitelisted-ips=");
        options.addArguments("--enable-logging", "--v=1", "--log-level=0");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // 왼쪽 패널
        JPanel leftPanel = createPanel(Color.WHITE);
        JLabel newsLabel = createTitleLabel("News");
        JPanel leftBottomPanel = createBottomPanel(Color.WHITE);
        leftPanel.add(newsLabel);
        leftPanel.add(leftBottomPanel);

        // WebDriver 초기화
        driver = new ChromeDriver(options);

        String newsUrl = "https://playvalorant.com/ko-kr/news/announcements/";

        // News 이미지 URLs + 링크 URLs 크롤링 + 타이틀 크롤링
        List<String> newsImageUrls = crawling(newsUrl, "picture.ContentListingCard-module--cardBannerWrapper--Fjxi0 img", "src");
        List<String> newsLinks = crawling(newsUrl, "a.ContentListingCard-module--contentListingCard--JqMck ", "href");
        List<String> newsTitles = crawling(newsUrl, "picture.ContentListingCard-module--cardBannerWrapper--Fjxi0 img", "alt");

        // 크롤링 정보를 담을 패널 배열
        JPanel[] leftCrawlPanels = new JPanel[4];

        for (int i = 0; i < 4; i++) {
            leftCrawlPanels[i] = createCrawlPanel();

            // 이미지 URL 사용
            String newsImageUrl = newsImageUrls.get(i);
            leftCrawlPanels[i].add(createImageLabel(newsImageUrl));

            // 패널에 클릭 이벤트 추가
            int finalI = i; // 클로저 문제 해결을 위해 final 변수로 저장
            leftCrawlPanels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // 클릭한 패널의 링크 가져오기
                    String clickedLink = newsLinks.get(finalI);
                    try {
                        // 링크 열기
                        Desktop.getDesktop().browse(new URI(clickedLink));
                    } catch (IOException | URISyntaxException exception) {
                        exception.printStackTrace();
                    }
                }
            });

            // 타이틀 텍스트
            String newsTitle = newsTitles.get(i);
            leftCrawlPanels[i].add(createMiniTitleLabel(newsTitle));

            leftCrawlPanels[i].revalidate();
            leftCrawlPanels[i].repaint();

            leftBottomPanel.add(leftCrawlPanels[i]);
            leftBottomPanel.add(Box.createRigidArea(new Dimension(0, 30)));  // 패널 사이 여백
        }

        // WebDriver 종료
        driver.quit();

        // 가운데 패널
        JPanel middlePanel = createPanel(Color.WHITE);
        JLabel gameUpdatesLabel = createTitleLabel("Game updates");
        JPanel middleBottomPanel = createBottomPanel(Color.WHITE);
        middlePanel.add(gameUpdatesLabel);
        middlePanel.add(middleBottomPanel);

        // WebDriver 초기화
        driver = new ChromeDriver(options);

        String gameUpdatesUrl ="https://playvalorant.com/ko-kr/news/game-updates/";

        // Game updates 이미지 URLs + 링크 URLs 크롤링 + 타이틀 크롤링
        List<String> gameUpdatesImageUrls = crawling(gameUpdatesUrl, "picture.ContentListingCard-module--cardBannerWrapper--Fjxi0 img", "src");
        List<String> gameUpdatesLinks = crawling(gameUpdatesUrl, "a.ContentListingCard-module--contentListingCard--JqMck ", "href");
        List<String> gameUpdatesTitles = crawling(gameUpdatesUrl, "picture.ContentListingCard-module--cardBannerWrapper--Fjxi0 img", "alt");

        // 크롤링 정보를 담을 패널 배열
        JPanel[] middleCrawlPanels = new JPanel[4];

        for (int i = 0; i < 4; i++) {
            middleCrawlPanels[i] = createCrawlPanel();

            // 이미지 URL 사용
            String gameUpdatesImageUrl = gameUpdatesImageUrls.get(i);
            middleCrawlPanels[i].add(createImageLabel(gameUpdatesImageUrl));

            // 패널에 클릭 이벤트 추가
            int finalI = i; // 클로저 문제 해결을 위해 final 변수로 저장
            middleCrawlPanels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // 클릭한 패널의 링크 가져오기
                    String clickedLink = gameUpdatesLinks.get(finalI);
                    try {
                        // 링크 열기
                        Desktop.getDesktop().browse(new URI(clickedLink));
                    } catch (IOException | URISyntaxException exception) {
                        exception.printStackTrace();
                    }
                }
            });

            // 타이틀 텍스트
            String gameUpdatesTitle = gameUpdatesTitles.get(i);
            middleCrawlPanels[i].add(createMiniTitleLabel(gameUpdatesTitle));

            middleCrawlPanels[i].revalidate();
            middleCrawlPanels[i].repaint();

            middleBottomPanel.add(middleCrawlPanels[i]);
            middleBottomPanel.add(Box.createRigidArea(new Dimension(0, 30)));  // 패널 사이 여백
        }

        // WebDriver 종료
        driver.quit();

        // 오른쪽 패널
        JPanel rightPanel = createPanel(Color.WHITE);
        JLabel esportsLabel = createTitleLabel("e-sports");
        JPanel rightBottomPanel = createBottomPanel(Color.WHITE);
        rightPanel.add(esportsLabel);
        rightPanel.add(rightBottomPanel);

        // WebDriver 초기화
        driver = new ChromeDriver(options);

        String esportsUrl = "https://playvalorant.com/ko-kr/news/esports/";

        // e-sports 이미지 URLs + 링크 URLs 크롤링 + 타이틀 크롤링
        List<String> esportsImageUrls = crawling(esportsUrl, "picture.ContentListingCard-module--cardBannerWrapper--Fjxi0 img", "src");
        List<String> esportsLinks = crawling(esportsUrl, "a.ContentListingCard-module--contentListingCard--JqMck ", "href");
        List<String> esportsTitles = crawling(esportsUrl, "picture.ContentListingCard-module--cardBannerWrapper--Fjxi0 img", "alt");

        // 크롤링 정보를 담을 패널 배열
        JPanel[] rightCrawlPanels = new JPanel[4];

        for (int i = 0; i < 4; i++) {
            rightCrawlPanels[i] = createCrawlPanel();

            // 이미지 URL 사용
            String esportsImageUrl = esportsImageUrls.get(i);
            rightCrawlPanels[i].add(createImageLabel(esportsImageUrl));

            // 패널에 클릭 이벤트 추가
            int finalI = i; // 클로저 문제 해결을 위해 final 변수로 저장
            rightCrawlPanels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // 클릭한 패널의 링크 가져오기
                    String clickedLink = esportsLinks.get(finalI);
                    try {
                        // 링크 열기
                        Desktop.getDesktop().browse(new URI(clickedLink));
                    } catch (IOException | URISyntaxException exception) {
                        exception.printStackTrace();
                    }
                }
            });

            // 타이틀 텍스트
            String esportsTitle = esportsTitles.get(i);
            rightCrawlPanels[i].add(createMiniTitleLabel(esportsTitle));

            rightCrawlPanels[i].revalidate();
            rightCrawlPanels[i].repaint();

            rightBottomPanel.add(rightCrawlPanels[i]);
            rightBottomPanel.add(Box.createRigidArea(new Dimension(0, 30)));  // 패널 사이 여백
        }

        // WebDriver 종료
        driver.quit();

        // 패널들을 mainPanel에 추가
        mainPanel.add(leftPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(20, 0)));  // 패널 사이의 가로 여백 조정
        mainPanel.add(middlePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(20, 0)));  // 패널 사이의 가로 여백 조정
        mainPanel.add(rightPanel);

        // NewsPage 패널에 mainPanel 추가
        add(mainPanel);
    }

    // Panel 생성 메서드
    private JPanel createPanel(Color backgroundColor) {
        JPanel panel = new JPanel();
        panel.setBackground(backgroundColor);
        panel.setPreferredSize(new Dimension(320, 850));
        panel.setMaximumSize(new Dimension(320, 850));
        panel.setMinimumSize(new Dimension(320, 850));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // 내부 여백 조정

        return panel;
    }

    // Title Label 생성 메서드
    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 25));
        return label;
    }

    // Bottom Panel 생성 메서드
    private JPanel createBottomPanel(Color backgroundColor) {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(backgroundColor);
        bottomPanel.setPreferredSize(new Dimension(300, 780));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));  // 내부 여백 조정
        return bottomPanel;
    }

    // 크롤링 정보 담을 패널 생성 메서드
    private JPanel createCrawlPanel() {
        JPanel crawlPanel = new JPanel();
        crawlPanel.setBackground(Color.WHITE);
        crawlPanel.setPreferredSize(new Dimension(280, 190));
        crawlPanel.setMaximumSize(new Dimension(280, 190));
        crawlPanel.setMinimumSize(new Dimension(280, 190));

        return crawlPanel;
    }

    // 크롤링 메서드
    private List<String> crawling(String url, String selector, String attribute) {
        driver.get(url);

        List<String> crawlUrls = new ArrayList<>();

        try {
            // elements 가져오기
            List<WebElement> crawlElements = driver.findElements(By.cssSelector(selector));

            for (WebElement crawlElement : crawlElements) {
                String crawlUrl = crawlElement.getAttribute(attribute);
                if (crawlUrl != null) {
                    System.out.println("Crawled URL: " + crawlUrl);
                    crawlUrls.add(crawlUrl);
                } else {
                    System.out.println("crawling 대상을 찾을 수 없습니다.");
                }
            }

        } finally {}

        return crawlUrls;
    }

    // 크롤링 패널 내부의 미니 타이틀 Label 생성 메서드
    private JLabel createMiniTitleLabel(String title) {
        JLabel titleLabel = new JLabel("<html>" + insertLineBreaks(title, 30) + "</html>");
        titleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
        titleLabel.setForeground(Color.BLACK);
        return titleLabel;
    }

    // 긴 문자열에 줄바꿈을 추가하는 메서드
    private String insertLineBreaks(String input, int maxLength) {
        StringBuilder result = new StringBuilder();
        int length = input.length();

        for (int i = 0; i < length; i += maxLength) {
            int end = Math.min(i + maxLength, length);
            result.append(input, i, end);
            if (end < length) {
                result.append("<br>");
            }
        }

        return result.toString();
    }

    // 크롤링한 이미지를 담은 레이블 생성 메서드
    private JLabel createImageLabel(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);

            // 이미지 레이블 생성
            ImageIcon imageIcon = new ImageIcon(getRoundedImage(image, 280, 145));
            JLabel imageLabel = new JLabel(imageIcon);

            return imageLabel;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 이미지 꼭짓점을 둥글게 깎는 메서드
    private Image getRoundedImage(BufferedImage image, int width, int height) {
        BufferedImage roundedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = roundedImage.createGraphics();

        // 둥근 테두리 설정
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE); // 이미지의 둥근 부분을 흰색으로 채우기
        g2.fillRoundRect(0, 0, width, height, 20, 20); // 여기서 20, 20은 둥근 정도를 조절하는 값

        // 이미지 그리기
        g2.setComposite(AlphaComposite.SrcIn);
        g2.drawImage(image, 0, 0, width, height, null);
        g2.dispose();

        return roundedImage;
    }

}

