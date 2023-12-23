import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RecruitPartyPage extends JPanel {
    private Map<Integer, String> agentInfoMap;
    private ArrayList<RecruitPost> postList;
    private JPanel postListPanel;
    // wishToHaveComboPanels 배열 생성
    private JComboBox<String>[] wishToHaveComboBoxes;
    private JPanel weHaveAgentsImagesPanel;
    private JPanel wishToHaveAgentsImagesPanel;
    private JPanel mainPanel;
    private User user;
    // 파일 이름
    private static final String FILE_NAME = "recruit_posts.txt";
    public RecruitPartyPage(User user) {
        this.user = user;
        postList = new ArrayList<>();
        wishToHaveComboBoxes = new JComboBox[5];
        postListPanel = new JPanel();
        postListPanel.setBackground(Color.WHITE);

        // 전체 패널
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(1500, 1000));

        // 모집 글쓰기 버튼 패널
        JPanel writeButtonPanel = new JPanel(new FlowLayout());

        // 모집 글쓰기 버튼 (Image 버튼으로 변경)
        ImageIcon writeIcon = createResizedImageIcon("/images/btn_upload_recruit.png", 140, 45); // 리사이징하여 생성
        JButton writeButton = new JButton(writeIcon);

        // 기존 버튼 형식이 안 보이도록 코드 추가
        writeButton.setBorderPainted(false);
        writeButton.setContentAreaFilled(false);

        writeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showWriteRecruitDialog();
            }
        });

        // writeButtonPanel을 오른쪽 정렬하고, 왼쪽에는 Glue를 추가하여 가운데 정렬
        writeButtonPanel.setLayout(new BoxLayout(writeButtonPanel, BoxLayout.X_AXIS));
        writeButtonPanel.add(Box.createHorizontalGlue());
        writeButtonPanel.add(writeButton);

        writeButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
        writeButtonPanel.setBackground(Color.WHITE);
        writeButtonPanel.setPreferredSize(new Dimension(100, 45));

        mainPanel.add(writeButtonPanel);

        // 요원 이미지 (1~23)
        agentInfoMap = new HashMap<>();
        agentInfoMap.put(1, new String("제트"));
        agentInfoMap.put(2, new String("레이즈"));
        agentInfoMap.put(3, new String("브리치"));
        agentInfoMap.put(4, new String("오멘"));
        agentInfoMap.put(5, new String("브림스톤"));
        agentInfoMap.put(6, new String("피닉스"));
        agentInfoMap.put(7, new String("세이지"));
        agentInfoMap.put(8, new String("소바"));
        agentInfoMap.put(9, new String("바이퍼"));
        agentInfoMap.put(10, new String("사이퍼"));
        agentInfoMap.put(11, new String("레이나"));
        agentInfoMap.put(12, new String("킬조이"));
        agentInfoMap.put(13, new String("스카이"));
        agentInfoMap.put(14, new String("요루"));
        agentInfoMap.put(15, new String("아스트라"));
        agentInfoMap.put(16, new String("케이/오"));
        agentInfoMap.put(17, new String("체임버"));
        agentInfoMap.put(18, new String("네온"));
        agentInfoMap.put(19, new String("페이드"));
        agentInfoMap.put(20, new String("하버"));
        agentInfoMap.put(21, new String("게코"));
        agentInfoMap.put(22, new String("데드록"));
        agentInfoMap.put(23, new String("아이소"));

        // 초기화면에서 파일 읽어오기
        loadRecruitPostsFromFile();

        add(mainPanel);
    }

    // 모집 글 작성 다이얼로그
    private void showWriteRecruitDialog() {
        JFrame writeRecruitFrame = new JFrame("모집 글 작성");
        writeRecruitFrame.setSize(500, 500);
        writeRecruitFrame.setLocationRelativeTo(null); // 화면 정중앙에 표시

        // 내용을 세로로 배치할 패널
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // 1-1 레이블
        JLabel label11 = new JLabel("We have");
        label11.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label11);

        // 1-2 레이블
        JLabel label12 = new JLabel("본인을 포함하여 파티원 인원에 맞게 선택해주세요.");
        label12.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label12);

        // weHaveMemberNComboPanels 배열 생성
        JPanel[] weHaveMemberNComboPanels = new JPanel[4];

        for (int i = 0; i < 4; i++) {
            // 레이블 생성 및 설정
            JLabel weHaveMemberLabel = new JLabel("파티원 " + (i + 1));

            // 콤보박스 생성 및 설정
            String[] weHaveAgentNames = {
                    "없음", "제트", "레이즈", "브리치", "오멘", "브림스톤",
                    "피닉스", "세이지", "소바", "바이퍼", "사이퍼",
                    "레이나", "킬조이", "스카이", "요루", "아스트라",
                    "케이/오", "체임버", "네온", "페이드", "하버",
                    "게코", "데드록", "아이소"
            };

            JComboBox<String> weHaveComboBox = new JComboBox<>(weHaveAgentNames);
            weHaveComboBox.setPreferredSize(new Dimension(100, 20));

            // 패널 생성 및 레이아웃 설정
            weHaveMemberNComboPanels[i] = new JPanel();
            weHaveMemberNComboPanels[i].setLayout(new FlowLayout());
            weHaveMemberNComboPanels[i].add(weHaveMemberLabel);
            weHaveMemberNComboPanels[i].add(weHaveComboBox);

            // 메인 패널에 추가
            panel.add(weHaveMemberNComboPanels[i]);
        }

        // 간격
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // 2-1 레이블
        JLabel label21 = new JLabel("Wish to have");
        label21.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label21);

        // 2-2 레이블
        JLabel label22 = new JLabel("함께하고 싶은 요원을 선택해주세요. (최대 5개)");
        label22.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label22);


        // wishToHaveComboPanels 배열 생성
        JPanel[] wishToHaveComboPanels = new JPanel[5];

        for (int i = 0; i < 5; i++) {
            // 콤보박스 생성 및 설정
            String[] wishToHaveAgentNames = {
                    "선택안함", "제트", "레이즈", "브리치", "오멘", "브림스톤",
                    "피닉스", "세이지", "소바", "바이퍼", "사이퍼",
                    "레이나", "킬조이", "스카이", "요루", "아스트라",
                    "케이/오", "체임버", "네온", "페이드", "하버",
                    "게코", "데드록", "아이소"
            };

            JComboBox<String> wishToHaveComboBox = new JComboBox<>(wishToHaveAgentNames);
            wishToHaveComboBox.setPreferredSize(new Dimension(100, 20));

            // 패널 생성 및 레이아웃 설정
            wishToHaveComboPanels[i] = new JPanel();
            wishToHaveComboPanels[i].setLayout(new FlowLayout());
            wishToHaveComboPanels[i].add(wishToHaveComboBox);

            // 콤보박스 배열에 추가
            wishToHaveComboBoxes[i] = wishToHaveComboBox;

            // 메인 패널에 추가
            panel.add(wishToHaveComboPanels[i]);
        }

        JButton uploadButton = new JButton("업로드");
        JButton cancelButton = new JButton("취소");

        // 업로드 버튼
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 확인 다이얼로그 표시
                int result = JOptionPane.showConfirmDialog(writeRecruitFrame,
                        "파티원 모집 글을 업로드 하시겠습니까?", "파티원 모집",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                // "예"를 선택한 경우
                if (result == JOptionPane.YES_OPTION) {
                    // 선택된 값들을 배열에 저장 (We Have)
                    String[] weHaveValues = new String[4];
                    for (int i = 0; i < 4; i++) {
                        // 이미 패널이므로 추가적인 형변환이 필요 없음
                        JPanel comboPanel = weHaveMemberNComboPanels[i];

                        // 패널 내에서 콤보박스 추출
                        JComboBox<String> weHaveComboBox = (JComboBox<String>) comboPanel.getComponent(1);

                        weHaveValues[i] = (String) weHaveComboBox.getSelectedItem();
                    }

                    // 선택된 값들을 배열에 저장 (Wish to Have)
                    // wishToHaveComboBoxes 배열을 통해 콤보박스에 접근하여 값 추출
                    String[] wishToHaveValues = new String[5];
                    for (int i = 0; i < 5; i++) {
                        wishToHaveValues[i] = (String) wishToHaveComboBoxes[i].getSelectedItem();
                    }

                    // '없음' 속성의 개수 세기
                    int noneCount = 0;
                    for (String value : weHaveValues) {
                        if ("없음".equals(value)) {
                            noneCount++;
                        }
                    }

                    // weHaveCount : 이미 모인 파티원의 수
                    int weHaveCount = 4 - noneCount;

                    // 모집 정보가 부족한 경우 (= 사용자가 전부 '없음'을 선택하거나 전부 '선택안함'을 선택한 경우)
                    if (weHaveCount == 0 || Arrays.stream(wishToHaveValues).allMatch(value -> "선택안함".equals(value))) {
                        JOptionPane.showMessageDialog(writeRecruitFrame,
                                "모집 정보가 부족합니다. 요원을 선택해주세요.",
                                "주의", JOptionPane.ERROR_MESSAGE);
                        return;  // 업로드하지 않음
                    }

                    // 새 모집글 내용 출력
                    System.out.println("- Writer: " + user.getFullName());
                    System.out.println("- We Have Count: " + weHaveCount);
                    System.out.println("- We have values: " + Arrays.toString(weHaveValues));
                    System.out.println("- Wish to have values: " + Arrays.toString(wishToHaveValues));

                    // RecruitPost 객체 생성
                    RecruitPost recruitPost = new RecruitPost(user.getFullName(), weHaveCount, weHaveValues, wishToHaveValues);

                    // 모집글을 파일에 추가
                    appendRecruitPostToFile(recruitPost);

                    // RecruitPost를 직접 postList에 추가
                    postList.add(recruitPost);

                    // 업데이트 메서드 호출
                    updateRecruitPostList();

                    writeRecruitFrame.dispose();

                }
            }
        });

        // 취소 버튼
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeRecruitFrame.dispose();
            }
        });

        // 버튼 패널
        JPanel uploadNcancelButtonPanel = new JPanel();
        uploadNcancelButtonPanel.setLayout(new FlowLayout());
        uploadNcancelButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 100)); // 내부 여백 설정
        uploadNcancelButtonPanel.add(uploadButton);
        uploadNcancelButtonPanel.add(cancelButton);
        panel.add(uploadNcancelButtonPanel);

        writeRecruitFrame.add(panel);
        writeRecruitFrame.setLocationRelativeTo(null);
        writeRecruitFrame.setVisible(true);

    }

    // 모집글을 파일에 추가하는 메서드
    public void appendRecruitPostToFile(RecruitPost recruitPost) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            // 모집글 정보를 파일에 추가
            writer.write("Writer: " + recruitPost.getWriter());
            writer.newLine();
            writer.write("We Have Count: " + recruitPost.getWeHaveCount());
            writer.newLine();

            // We Have Agents 정보
            writer.write("We Have Agents: ");
            for (String agent : recruitPost.getWeHaveAgents()) {
                writer.write(agent + " ");
            }
            writer.newLine();

            // Wish To Have Agents 정보
            writer.write("Wish To Have Agents: ");
            for (String agent : recruitPost.getWishToHaveAgents()) {
                writer.write(agent + " ");
            }
            writer.newLine();

            // 구분을 위한 빈 줄 추가
            writer.newLine();

            System.out.println("새로운 모집글이 파일에 추가되었습니다.");

        } catch (IOException e) {
            System.err.println("파일에 새로운 모집글을 추가하는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 파일에서 모집글을 읽어오는 메서드
    private void loadRecruitPostsFromFile() {
        Path filePath = Paths.get(FILE_NAME);

        try {
            // 파일이 존재하는지 확인
            if (!Files.exists(filePath)) {
                // 파일이 존재하지 않으면 새 파일 생성
                Files.createFile(filePath);
                System.out.println("파일을 생성했습니다: " + FILE_NAME);
            } else {
                System.out.println("파일이 존재합니다: " + FILE_NAME);
            }
        } catch (FileAlreadyExistsException e) {
            System.err.println("파일이 이미 존재합니다: " + FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 파일에서 읽어온 모집글 목록을 화면에 업데이트
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            StringBuilder postContent = new StringBuilder(); // 줄바꿈 처리를 위한 StringBuilder 사용
            while ((line = reader.readLine()) != null) {
                postContent.append(line).append(System.lineSeparator());

                // 빈 줄이 나오면 현재까지 읽은 내용을 모집글로 처리
                if (line.isEmpty()) {
                    processPostContent(postContent.toString());

                    // StringBuilder 초기화
                    postContent.setLength(0);
                }
            }

            // 마지막에 읽은 모집글 처리
            if (postContent.length() > 0) {
                processPostContent(postContent.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 모집글 내용 분리
    private void processPostContent(String content) {
        // 각 필드를 초기화
        String writer = "";
        int weHaveCount = 0;
        String[] weHaveAgents = null;
        String[] wishToHaveAgents = null;

        // 현재 어떤 필드를 읽고 있는지 나타내는 상태 변수
        int currentState = 0; // 0: writer, 1: weHaveCount, 2: weHaveAgents, 3: wishToHaveAgents

        // Split the content by the end markers
        String[] lines = content.split(System.lineSeparator());
        for (String line : lines) {
            // 기준 문자열로 시작하면 상태를 변경
            if (line.startsWith("Writer:")) {
                currentState = 0;
                writer = line.substring("Writer:".length()).trim();
                System.out.println("파일에서 읽어온 Writer: " + writer);
            } else if (line.startsWith("We Have Count:")) {
                currentState = 1;
                weHaveCount = Integer.parseInt(line.substring("We Have Count:".length()).trim());
                System.out.println("파일에서 읽어온 We Have Count: " + weHaveCount);
            } else if (line.startsWith("We Have Agents:")) {
                currentState = 2;
                // We Have Agents
                String agentsLine = line.substring("We Have Agents:".length()).trim();
                weHaveAgents = agentsLine.split("\\s+");
                System.out.println("파일에서 읽어온 We Have Agents: " + Arrays.toString(weHaveAgents));
            } else if (line.startsWith("Wish To Have Agents:")) {
                currentState = 3;
                // Wish To Have Agents
                String wishLine = line.substring("Wish To Have Agents:".length()).trim();
                wishToHaveAgents = wishLine.split("\\s+");
                System.out.println("파일에서 읽어온 Wish To Have Agents: " + Arrays.toString(wishToHaveAgents));
            } else {
                // 현재 필드에 따라 처리
                switch (currentState) {
                    case 2:
                        // We Have Agents에 해당하는 라인 처리
                        // 여기에서 weHaveAgents 배열에 각 요소를 추가
                        weHaveAgents = line.trim().split("\\s+");
                        break;
                    case 3:
                        // Wish To Have Agents에 해당하는 라인 처리
                        // 여기에서 wishToHaveAgents 배열에 각 요소를 추가
                        wishToHaveAgents = line.trim().split("\\s+");
                        break;
                    default:
                        // 다른 필드의 경우에는 추가적인 처리를 수행할 수 있음
                        break;
                }
            }
        }

        // RecruitPost 객체 생성
        RecruitPost recruitPost = new RecruitPost(writer, weHaveCount, weHaveAgents, wishToHaveAgents);

        // postList에 추가
        postList.add(recruitPost);

        // 업데이트 메서드 호출
        updateRecruitPostList();
    }

    // 모집글 리스트를 업데이트하는 메서드
    private void updateRecruitPostList() {
        // postListPanel 초기화
        postListPanel.removeAll();
        postListPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 10)); // 왼쪽 정렬, 수평 및 수직 간격 설정

        // postList에 있는 모집글 목록을 순회하며 추가
        for (RecruitPost post : postList) {
            // 모집글을 표시할 패널 생성
            JPanel postPanel = createRecruitPostPanel(post);

            // postListPanel에 패널 추가
            postListPanel.add(postPanel);
        }

        // 변경된 내용을 반영
        postListPanel.revalidate();
        postListPanel.repaint();

        // mainPanel에 추가
        mainPanel.add(postListPanel);

        // 추가된 내용을 반영
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /// RecruitPost를 표시하는 패널을 생성하는 메서드
    private JPanel createRecruitPostPanel(RecruitPost post) {
        // 테두리가 있는 패널 생성
        JPanel postPanel = new JPanel();
        postPanel.setBackground(Color.WHITE);
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        postPanel.setPreferredSize(new Dimension(400, 200)); // 크기 조절
        postPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK), // 테두리
                BorderFactory.createEmptyBorder(20, 20, 20, 20) // 내부 여백
        ));

        // writerLabel
        JLabel writerLabel = new JLabel(post.getWriter());
        writerLabel.setFont(new Font("맑은 고딕", Font.BOLD, 17)); // 폰트 설정

        // weHaveCountLabel
        JLabel weHaveCountLabel = new JLabel("  (" + post.getWeHaveCount() + "/5)");
        weHaveCountLabel.setFont(new Font("SansSerif", Font.BOLD, 17)); // 폰트 설정

        // writerNcountPanel : wrter + weHaveCount
        JPanel writerNcountPanel = new JPanel();
        writerNcountPanel.setLayout(new FlowLayout());
        writerNcountPanel.setPreferredSize(new Dimension(150, 20));
        writerNcountPanel.setBackground(Color.WHITE);
        writerNcountPanel.add(writerLabel);
        writerNcountPanel.add(weHaveCountLabel);
        postPanel.add(writerNcountPanel);
        postPanel.add(Box.createRigidArea(new Dimension(0, 20))); // 수직 간격 추가

        // textPanel : 두 텍스트 label을 세로로 묶는 패널
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // 왼쪽 정렬
        textPanel.setBackground(Color.WHITE);
        textPanel.add(new JLabel("We Have Agents:"));
        textPanel.add(Box.createRigidArea(new Dimension(0, 40))); // 수직 간격 추가
        textPanel.add(new JLabel("Wish To Have Agents:"));
        textPanel.add(Box.createRigidArea(new Dimension(0, 10))); // 수직 간격 추가

        // We Have Agents 표시
        String[] weHaveAgents = post.getWeHaveAgents();
        weHaveAgentsImagesPanel = new JPanel();
        weHaveAgentsImagesPanel.removeAll(); // 초기화
        createAgentsImagesPanel(weHaveAgents, weHaveAgentsImagesPanel);
        weHaveAgentsImagesPanel.setBackground(Color.WHITE);

        // Wish To Have Agents 표시
        String[] wishToHaveAgents = post.getWishToHaveAgents();
        wishToHaveAgentsImagesPanel = new JPanel();
        wishToHaveAgentsImagesPanel.removeAll(); // 초기화
        createAgentsImagesPanel(wishToHaveAgents, wishToHaveAgentsImagesPanel);
        wishToHaveAgentsImagesPanel.setBackground(Color.WHITE);

        // imagesPanel : weHaveAgentsImagesPanel + wishToHaveAgentsImagesPanel 세로로 묶은 패널
        JPanel imagesPanel = new JPanel();
        imagesPanel.setLayout(new BoxLayout(imagesPanel, BoxLayout.Y_AXIS));
        imagesPanel.add(weHaveAgentsImagesPanel);
        imagesPanel.add(wishToHaveAgentsImagesPanel);

        // miniBottomPanel : textPanel + imagesPanel 가로로 묶은 패널
        JPanel miniBottomPanel = new JPanel();
        miniBottomPanel.setLayout(new BoxLayout(miniBottomPanel, BoxLayout.X_AXIS));
        miniBottomPanel.setBackground(Color.WHITE);
        miniBottomPanel.add(textPanel);
        miniBottomPanel.add(imagesPanel);

        // postPanel에 miniBottomPanel 추가
        postPanel.add(miniBottomPanel);

        return postPanel;
    }

    // 요원 이미지 패널 생성
    private JPanel createAgentsImagesPanel(String[] agents, JPanel targetPaenl) {
        // 기존 패널이 존재하는 경우 패널을 초기화
        if (targetPaenl != null) {
            targetPaenl.removeAll();
        } else {
            // 존재하지 않는 경우 패널을 생성
            targetPaenl = new JPanel();
            targetPaenl.setLayout(new BoxLayout(targetPaenl, BoxLayout.Y_AXIS));
        }

        for (String agentName : agents) {
            // "없음" 또는 "선택안함"이면 처리 건너뛰기
            if ("없음".equals(agentName) || "선택안함".equals(agentName)) {
                continue;
            }

            // 이미지 아이콘을 가져오는 메서드
            ImageIcon agentIcon = createAgentIcon(agentName);

            // 이미지 아이콘을 라벨에 넣어 패널에 추가
            JLabel agentLabel = new JLabel(agentIcon);
            targetPaenl.add(agentLabel);

        }

        return targetPaenl;
    }

    // 이미지 아이콘을 가져오는 메서드
    private ImageIcon createAgentIcon(String agentName) {
        // agentInfoMap에서 에이전트 이름에 해당하는 번호를 찾아 반환
        int agentNumber = getAgentNumber(agentName);

        // 에이전트 번호에 해당하는 이미지 경로 생성
        String imagePath = "/images/agent" + agentNumber + ".png";

        // 이미지 아이콘을 가져오기
        ImageIcon icon = createResizedImageIcon(imagePath, 40, 40);
        if (icon == null) {
            System.err.println("이미지 아이콘 생성에 실패했습니다. 경로: " + imagePath);
        }

        return icon;
    }

    // agentInfoMap에서 에이전트 이름에 해당하는 번호를 찾아 반환
    private int getAgentNumber(String agentName) {
        for (Map.Entry<Integer, String> entry : agentInfoMap.entrySet()) {
            if (entry.getValue().equals(agentName)) {
                return entry.getKey();
            }
        }
        return -1;  // 매칭되는 에이전트가 없을 경우 -1 반환
    }

    // 이미지 아이콘을 생성하는 메서드
    private ImageIcon createImageIcon(String path) {
        // 이미지 아이콘을 생성하고 반환
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("이미지를 찾을 수 없습니다: " + path);
            return null;
        }
    }

    // 이미지 아이콘을 리사이징하여 생성하는 메서드
    private ImageIcon createResizedImageIcon(String path, int width, int height) {
        ImageIcon originalIcon = createImageIcon(path);
        if (originalIcon != null) {
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        } else {
            return null;
        }
    }

    // RecruitPost 클래스
    public class RecruitPost {
        private String writer;
        private int weHaveCount;
        private String[] weHaveAgents;
        private String[] wishToHaveAgents;

        public RecruitPost(String writer, int weHaveCount, String[] weHaveAgents, String[] wishToHaveAgents) {
            this.writer = writer;
            this.weHaveCount = weHaveCount;
            this.weHaveAgents = weHaveAgents;
            this.wishToHaveAgents = wishToHaveAgents;
        }

        public String getWriter() { return writer; }

        public int getWeHaveCount() { return weHaveCount; }

        public String[] getWeHaveAgents() { return weHaveAgents; }

        public String[] getWishToHaveAgents() { return wishToHaveAgents; }

    }

}