import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private static List<JLabel> redBarLabels = new ArrayList<>();
    private static CardLayout cardLayout;
    private static JPanel menuPages;
    private static User user; // User 클래스의 인스턴스 추가
    private static JFrame frame;

    public static void main(String[] args) {
        // 첫 화면
        System.out.println("< 로그인 시도 >");

        // user 초기화
        user = new User("","");
        user.logIn();
    }

    public static void createAndShowGUI(User user) {
        // 이전 창이 이미 열려 있다면 닫기
        if (frame != null) {
            frame.dispose();
        }

        frame = new JFrame("Valorant PLUS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 1000);

        // 'valorant_black_logo.png' 이미지를 창 아이콘으로 설정
        ImageIcon valorantBlackIcon = new ImageIcon(Menu.class.getResource("/images/valorant_black_logo.png"));
        frame.setIconImage(valorantBlackIcon.getImage());

        // 상단 메뉴바 패널
        JPanel menuBar = new JPanel();
        menuBar.setBackground(Color.BLACK);
        menuBar.setLayout(new FlowLayout());

        // riotIcon, valorantIcon 이미지 아이콘 생성
        ImageIcon riotIcon = new ImageIcon(Menu.class.getResource("/images/riot_logo.png"));
        ImageIcon valorantIcon = new ImageIcon(Menu.class.getResource("/images/valorant_logo.png"));

        // 'riot_logo.png' 이미지 표시
        JLabel riotLabel = new JLabel(riotIcon);
        riotLabel.setOpaque(true);
        riotLabel.setBackground(Color.BLACK);
        menuBar.add(riotLabel);

        // 'valorant_logo.png' 이미지 표시
        JLabel valorantLabel = new JLabel(valorantIcon);
        valorantLabel.setOpaque(true);
        valorantLabel.setBackground(Color.BLACK);
        menuBar.add(valorantLabel);

        // createMenuPanel을 통해 6개의 메뉴 버튼 생성
        redBarLabels.add(createMenuPanel("My Profile", menuBar, "/images/my_profile_menu.png"));
        redBarLabels.add(createMenuPanel("Recruit Party", menuBar, "/images/recruit_party_menu.png"));
        redBarLabels.add(createMenuPanel("Combination", menuBar, "/images/combination_menu.png"));
        redBarLabels.add(createMenuPanel("Community", menuBar, "/images/community_menu.png"));
        redBarLabels.add(createMenuPanel("News", menuBar, "/images/news_menu.png"));
        redBarLabels.add(createMenuPanel("Message", menuBar, "/images/message_menu.png"));

        // 로그아웃 버튼
        ImageIcon logoutIcon = new ImageIcon(Menu.class.getResource("/images/logout_menu.png"));
        JButton logoutButton = new JButton(logoutIcon);
        logoutButton.setOpaque(true);
        logoutButton.setBackground(Color.BLACK);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setContentAreaFilled(false);
        menuBar.add(logoutButton);

        // 로그아웃 버튼에 ActionListener 추가
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("< 로그아웃 및 로그인 시도 >");

                // user 클래스의 logIn() 호출
                user.logIn();
            }
        });

        frame.getContentPane().add(menuBar, BorderLayout.NORTH);

        // 페이지 초기화
        MyProfilePage myProfilePage = new MyProfilePage(user);
        RecruitPartyPage recruitPartyPage = new RecruitPartyPage(user);
        CombinationPage combinationPage = new CombinationPage();
        CommunityPage communityPage = new CommunityPage(user);
        NewsPage newsPage = new NewsPage();
        MessagePage messagePage = new MessagePage(user);

        // 페이지 관리를 위한 CardLayout
        cardLayout = new CardLayout();
        menuPages = new JPanel(cardLayout);

        // 페이지를 CardLayout에 추가
        menuPages.add(myProfilePage, "My Profile");
        menuPages.add(recruitPartyPage, "Recruit Party");
        menuPages.add(combinationPage, "Combination");
        menuPages.add(communityPage, "Community");
        menuPages.add(newsPage, "News");
        menuPages.add(messagePage, "Message");

        frame.add(menuPages, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    /// 메뉴를 각각 redBarLabel과 엮어 각각의 패널 생성
    private static JLabel createMenuPanel(String menuName, JPanel menuBar, String imagePath) {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());
        menuPanel.setBackground(Color.BLACK);

        JButton menuButton = new JButton(new ImageIcon(Menu.class.getResource(imagePath)));
        menuButton.setContentAreaFilled(false);
        menuButton.setBorderPainted(false);
        menuButton.setFocusPainted(false);

        // redBar
        ImageIcon redBarIcon;
        if (menuName.equals("Message")) {
            // 'Message' 메뉴의 redBar는 짧은 길이의 이미지 사용
            redBarIcon = new ImageIcon(Menu.class.getResource("/images/red_bar_small.png"));
        } else {
            // 다른 메뉴들에는 기본 redBar 이미지 사용
            redBarIcon = new ImageIcon(Menu.class.getResource("/images/red_bar.png"));
        }

        JLabel redBarLabel = new JLabel(redBarIcon);
        redBarLabel.setOpaque(true);
        redBarLabel.setBackground(Color.BLACK);
        menuPanel.add(redBarLabel, BorderLayout.SOUTH);

        // 'My Profile' 메뉴는 기본적으로 redBar 활성화 (첫 화면이므로)
        if (menuName.equals("My Profile")) {
            redBarLabel.setVisible(true);
        } else {
            redBarLabel.setVisible(false);
        }

        menuPanel.add(menuButton, BorderLayout.NORTH);

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JLabel label : redBarLabels) {
                    label.setVisible(false);
                }
                // 메뉴를 클릭하면: 해당 redBarLabel 활성화 + 페이지 전환
                redBarLabel.setVisible(true);
                cardLayout.show(menuPages, menuName);
            }
        });

        menuBar.add(menuPanel);

        return redBarLabel;
    }

}
