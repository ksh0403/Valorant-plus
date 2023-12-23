import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.swing.*;

public class User {
    private String playerName;
    private String tag;
    private String encodedUrl;

    public User(String playerName, String tag) {
        this.playerName = playerName;
        this.tag = tag;
        this.encodedUrl = generateEncodedUrl(playerName, tag);
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getTag() {
        return tag;
    }

    public String getEncodedUrl() {
        return encodedUrl;
    }

    public String getFullName() {
        return playerName + "#" + tag;
    }

    // playerName과 tag로 인코딩된 URL을 생성하는 함수
    private String generateEncodedUrl(String playerName, String tag) {
        try {
            String encodedPlayerName = URLEncoder.encode(playerName, "UTF-8");
            String encodedTag = URLEncoder.encode(tag, "UTF-8");
            return "https://valorant.op.gg/profile/" + encodedPlayerName + "-" + encodedTag;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("인코딩 URL 생성 실패 ...");
            return null;
        }
    }

    // 로그인 함수
    public void logIn() {
        // 입력 다이얼로그 생성
        JTextField playerNameField = new JTextField();
        JTextField tagField = new JTextField();

        Object[] message = {
                "플레이어 이름:", playerNameField,
                "태그:", tagField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "로그인 정보를 입력해주세요", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            // 사용자가 확인을 눌렀을 때의 처리
            String playerName = playerNameField.getText();
            String tag = tagField.getText();

            // User 클래스에 정보 저장
            this.playerName = playerName;
            this.tag = tag;
            this.encodedUrl = generateEncodedUrl(playerName, tag);

            // 입력된 정보
            System.out.println("플레이어 이름: " + getPlayerName());
            System.out.println("태그: " + getTag());
            System.out.println("인코딩 URL 생성: " + getEncodedUrl());

            // createAndShowGUI 호출
            SwingUtilities.invokeLater(() -> Menu.createAndShowGUI(this));
        } else {
            // 사용자가 취소를 눌렀을 때의 처리
            System.out.println("사용자가 취소를 눌렀습니다.");
        }
    }

}
