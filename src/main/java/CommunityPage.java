import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CommunityPage extends JPanel {
    private ArrayList<Post> postList;
    private JPanel postListPanel;
    private JLabel infoTextLabel;
    private User user;

    // 파일 경로
    private static final String FILE_NAME = "community_posts.txt";

    public CommunityPage(User user) {
        this.user = user;
        postList = new ArrayList<>();

        // 전체 패널
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(1500, 1000));

        // 글쓰기 버튼 패널
        JPanel writeButtonPanel = new JPanel(new FlowLayout());

        // 글쓰기 버튼 (Image 버튼으로 변경)
        ImageIcon writeIcon = createResizedImageIcon("/images/btn_upload_post.png", 110, 45); // 리사이징하여 생성
        JButton writeButton = new JButton(writeIcon);

        // 기존 버튼 형식이 안 보이도록 코드 추가
        writeButton.setBorderPainted(false);
        writeButton.setContentAreaFilled(false);

        writeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showWritePostDialog();
            }
        });

        // writeButtonPanel을 오른쪽 정렬하고, 왼쪽에는 Glue를 추가하여 가운데 정렬
        writeButtonPanel.setLayout(new BoxLayout(writeButtonPanel, BoxLayout.X_AXIS));
        writeButtonPanel.add(Box.createHorizontalGlue());
        writeButtonPanel.add(writeButton);

        writeButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 140));
        writeButtonPanel.setBackground(Color.WHITE);
        writeButtonPanel.setPreferredSize(new Dimension(100, 45));

        // infoTextLabel
        infoTextLabel = new JLabel("업로드된 게시글이 없습니다. '글쓰기' 버튼을 눌러 게시글을 작성해보세요.");
        infoTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoTextLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // 여백 설정
        infoTextLabel.setForeground(Color.GRAY);
        infoTextLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트 설정

        // 글 목록 패널
        postListPanel = new JPanel();
        postListPanel.setLayout(new BoxLayout(postListPanel, BoxLayout.Y_AXIS));
        postListPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        postListPanel.setBackground(Color.WHITE);
        postListPanel.setPreferredSize(new Dimension(700, 800));

        mainPanel.add(Box.createVerticalGlue()); // 높이 자동 조절 및 수직 가운데 정렬
        mainPanel.add(writeButtonPanel);
        mainPanel.add(infoTextLabel);
        mainPanel.add(postListPanel);
        mainPanel.add(Box.createVerticalGlue()); // 높이 자동 조절 및 수직 가운데 정렬

        // 초기화면에서 파일 읽어오기
        loadPostsFromFile();

        add(mainPanel);
    }

    // 게시글을 파일에 추가하는 메서드
    private void appendPostToFile(Post post) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            // 각 필드를 '$'로 감싸고 마커를 추가하여 저장
            writer.write("$제목$" + post.getTitle() + System.lineSeparator() +
                    "$글쓴이$" + post.getWriter() + System.lineSeparator() +
                    "$작성일자$" + post.getUploadDate() + System.lineSeparator() +
                    "$내용$" + post.getContent() + System.lineSeparator() +
                    "$끝$" + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일에서 게시글을 읽어오는 메서드
    private void loadPostsFromFile() {
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

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            StringBuilder postContent = new StringBuilder(); // 줄바꿈 처리를 위한 StringBuilder 사용
            while ((line = reader.readLine()) != null) {
                postContent.append(line).append(System.lineSeparator());
            }

            // 게시글 처리
            if (postContent.length() > 0) {
                processPostContent(postContent.toString());
                // 파일에서 읽어온 게시글 목록을 최신순으로 정렬
                postList.sort((post1, post2) -> post2.getUploadDate().compareTo(post1.getUploadDate()));
                // 변경된 내용을 반영하여 화면 업데이트
                updatePostList();
                // info text 지우기
                infoTextLabel.setText(" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 파일에서 읽어온 게시글 목록을 화면에 업데이트
        updatePostList();
    }

    // 게시글 내용 분리
    private void processPostContent(String content) {
        // 각 필드를 초기화
        String title = "";
        String writer = "";
        String uploadDate = "";
        StringBuilder postContent = new StringBuilder();

        // 현재 어떤 필드를 읽고 있는지 나타내는 상태 변수
        int currentState = 0; // 0: title, 1: writer, 2: uploadDate, 3: postContent

        // Split the content by the end markers
        String[] lines = content.split(System.lineSeparator());
        for (String line : lines) {
            // 현재 읽고 있는 필드에 문자를 추가
            switch (currentState) {
                case 0:
                    if (line.startsWith("$제목$")) {
                        title = line.substring("$제목$".length()); // '$제목$' 이후의 문자열을 title에 저장
                        currentState = 1; // '$제목$'을 읽으면 다음 필드로 이동
                    }
                    break;
                case 1:
                    if (line.startsWith("$글쓴이$")) {
                        writer = line.substring("$글쓴이$".length()); // '$글쓴이$' 이후의 문자열을 writer에 저장
                        currentState = 2; // '$글쓴이$'을 읽으면 다음 필드로 이동
                    }
                    break;
                case 2:
                    if (line.startsWith("$작성일자$")) {
                        uploadDate = line.substring("$작성일자$".length()); // '$작성일자$' 이후의 문자열을 uploadDate에 저장
                        currentState = 3; // '$작성일자$'을 읽으면 다음 필드로 이동
                    }
                    break;
                case 3:
                    if (line.startsWith("$내용$")) {
                        postContent.append(line.substring("$내용$".length())).append(System.lineSeparator());
                        currentState = 4; // '$내용$'을 읽으면 다음 필드로 이동
                    }
                    break;
                case 4:
                    if (line.startsWith("$끝$")) {
                        // 마지막 라인이면 게시글 추가 후 상태 초기화
                        Post post = new Post(title, postContent.toString(), uploadDate, writer);
                        postList.add(post);
                        title = "";
                        writer = "";
                        uploadDate = "";
                        postContent = new StringBuilder();
                        currentState = 0;
                    } else {
                        postContent.append(line).append(System.lineSeparator()); // '$끝$' 이후가 아니면 줄바꿈 문자를 추가하여 내용 연결
                    }
                    break;
            }
        }
    }

    // 게시글 작성 다이얼로그
    private void showWritePostDialog() {
        JFrame writePostFrame = new JFrame("게시글 작성");
        writePostFrame.setSize(500, 350);
        writePostFrame.setLocationRelativeTo(null); // 화면 정중앙에 표시

        JTextField titleField = new JTextField();
        titleField.setPreferredSize(new Dimension(300, 40));
        titleField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // 내부 여백 설정

        JTextArea contentArea = new JTextArea();
        contentArea.setPreferredSize(new Dimension(300, 200));
        contentArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 내부 여백 설정

        JButton uploadButton = new JButton("업로드");
        JButton cancelButton = new JButton("취소");

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String content = contentArea.getText();
                String uploadDate = getFormattedCurrentDate();  // 현재 시간을 가져와서 포맷팅

                // 글을 업로드하고 목록에 추가
                uploadPost(title, content, uploadDate);
                writePostFrame.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writePostFrame.dispose();
            }
        });

        // 다이얼로그 패널
        JPanel writePostPanel = new JPanel();
        writePostPanel.setLayout(new BoxLayout(writePostPanel, BoxLayout.Y_AXIS));
        writePostPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // 전체 여백 추가
        writePostPanel.add(Box.createRigidArea(new Dimension(0, 5))); // 상단 여백 추가
        writePostPanel.add(new JLabel("제목"));
        writePostPanel.add(Box.createRigidArea(new Dimension(0, 5))); // 상단 여백 추가
        writePostPanel.add(titleField);
        writePostPanel.add(Box.createRigidArea(new Dimension(0, 10))); // 상단 여백 추가
        writePostPanel.add(new JLabel("내용"));
        writePostPanel.add(Box.createRigidArea(new Dimension(0, 5))); // 상단 여백 추가
        writePostPanel.add(new JScrollPane(contentArea));
        writePostPanel.add(Box.createRigidArea(new Dimension(0, 5))); // 상단 여백 추가

        // uploadButton과 cancelButton을 묶는 패널
        JPanel uploadNcancelButtonPanel = new JPanel();
        uploadNcancelButtonPanel.setLayout(new BoxLayout(uploadNcancelButtonPanel, BoxLayout.X_AXIS));
        uploadNcancelButtonPanel.add(Box.createRigidArea(new Dimension(150, 0))); // 좌측 여백 추가
        uploadNcancelButtonPanel.add(uploadButton);
        uploadNcancelButtonPanel.add(Box.createHorizontalGlue()); // 가운데 정렬 공간
        uploadNcancelButtonPanel.add(cancelButton);
        uploadNcancelButtonPanel.add(Box.createRigidArea(new Dimension(150, 0))); // 우측 여백 추가

        writePostPanel.add(uploadNcancelButtonPanel);

        writePostFrame.add(writePostPanel);
        writePostFrame.setLocationRelativeTo(null);
        writePostFrame.setVisible(true);
    }

    // 게시글을 업로드하는 메서드
    private void uploadPost(String title, String content, String uploadDate) {
        String writer = user.getFullName();
        Post post = new Post(title, content, uploadDate, writer);
        postList.add(post);

        // 최신순으로 정렬된 게시글을 보여주기 위해 createReversePostList 호출
        createReversePostList();

        // 파일에도 추가
        appendPostToFile(post);

        // info text 지우기
        infoTextLabel.setText(" ");
    }

    // 게시글 리스트를 최신순으로 정렬하는 메서드
    private void createReversePostList() {
        // postList를 최신순으로 정렬
        postList.sort((post1, post2) -> post2.getUploadDate().compareTo(post1.getUploadDate()));

        // 변경된 내용을 반영
        updatePostList();
    }

    // 게시글 리스트를 업데이트하는 메서드
    private void updatePostList() {
        // postListPanel 초기화
        postListPanel.removeAll();

        // postList에 있는 글 목록을 순회하며 추가
        for (Post post : postList) {
            JButton postButton = new JButton(
                    "<html><div style='display: flex; flex-direction: column; height: 100%;'>" +
                            "<div style='text-align: left; width: 500px; color: white;'>" + post.getTitle() + "</div>" +
                            "<div style='text-align: right; width: 500px; color: white;'>" + post.getUploadDate() + "</div>" +
                            "</div></html>");

            postButton.addActionListener(new PostClickListener(post));   // 클릭 이벤트 리스너 추가
            Font font = new Font("맑은 고딕", Font.PLAIN, 15);
            postButton.setFont(font);   // 폰트 변경
            postButton.setBackground(Color.DARK_GRAY); // 배경색 변경
            postButton.setForeground(Color.WHITE); // 폰트 색 변경
            postButton.setFocusPainted(false);  // 포커스 간격을 없애서 버튼 외곽선이 보이지 않도록 함

            // 목록 하나의 크기 설정
            postButton.setPreferredSize(new Dimension(1000, 60));
            postButton.setMaximumSize(new Dimension(1000, 60));
            postButton.setMinimumSize(new Dimension(1000, 60));

            // 가운데 정렬을 위한 패널 생성
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
            buttonPanel.setBackground(Color.WHITE);

            buttonPanel.add(Box.createHorizontalGlue());  // 좌측 여백
            buttonPanel.add(postButton);
            buttonPanel.add(Box.createHorizontalGlue());  // 우측 여백
            postListPanel.add(buttonPanel);

            // postListPanel 간 수직 여백 추가
            postListPanel.add(Box.createVerticalStrut(5)); // 여백 추가
        }

        // 변경된 내용을 반영
        postListPanel.revalidate();
        postListPanel.repaint();
    }

    private class PostClickListener implements ActionListener {
        private Post post;

        public PostClickListener(Post post) {
            this.post = post;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            showPostDetails(post);
        }
    }

    // 게시글 확인 다이얼로그
    private void showPostDetails(Post post) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // 내부 여백 추가

        JLabel titleLabel = new JLabel("제목: " + post.getTitle());
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0)); // 하단 여백 추가

        JLabel writerLabel = new JLabel("글쓴이: " + post.getWriter());
        writerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0)); // 하단 여백 추가

        JLabel dateLabel = new JLabel("작성 일자: " + post.getUploadDate());
        dateLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // 하단 여백 추가

        JTextArea contentArea = new JTextArea(post.getContent());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10), // 내부 여백 설정
                BorderFactory.createEmptyBorder() // 테두리 삭제
        ));
        contentArea.setPreferredSize(new Dimension(500, 300)); // contentArea 크기 조정

        panel.add(titleLabel);
        panel.add(writerLabel);
        panel.add(dateLabel);
        panel.add(new JScrollPane(contentArea));

        JOptionPane.showMessageDialog(this, panel, "게시글 내용 확인", JOptionPane.PLAIN_MESSAGE);
    }

    // 현재 날짜를 가져와서 포맷팅하는 메서드
    private String getFormattedCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new java.util.Date());
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

    // Post 클래스
    private static class Post {
        private String title;       // 제목
        private String content;     // 내용
        private String uploadDate;  // 업로드 일자
        private String writer;      // 글쓴이

        public Post(String title, String content, String uploadDate, String writer) {
            this.title = title;
            this.content = content;
            this.uploadDate = uploadDate;
            this.writer = writer;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getUploadDate() {
            return uploadDate;
        }

        public String getWriter() {
            return writer;
        }

        @Override
        public String toString() {
            return title + " - " + uploadDate;
        }
    }

}
