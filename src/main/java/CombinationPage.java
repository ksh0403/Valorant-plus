import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class CombinationPage extends JPanel {
    private JButton[] selectedAgentButtons;
    private JPanel selectedAgentPanel;
    private JLabel infoLabel_AgName;
    private JLabel infoLabel_AgPosition;
    private Map<Integer, String[]> agentInfoMap;
    private JPanel buttonPanel;
    private JLabel instructionLabel;
    private JPanel selectedCombinationPanel;
    private JLabel selectedCombinationLabel;
    private String[] agentPositions;
    private AgentButtonListener agentButtonListener;

    public CombinationPage() {
        // 전체 패널
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 여백 설정
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(1500, 1000));

        // 요원 선택 안내 텍스트
        instructionLabel = new JLabel("요원을 선택하세요.");
        instructionLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 가운데 정렬
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0)); // 여백 설정
        mainPanel.add(instructionLabel);

        // selectedAgentButtons 배열 초기화
        selectedAgentButtons = new JButton[5];

        // agentPositions 배열 초기화
        agentPositions = new String[selectedAgentButtons.length];

        // selectedAgentPanel
        selectedAgentPanel = new JPanel();
        selectedAgentPanel.setLayout(new FlowLayout()); // 가로로 정렬되도록 설정
        selectedAgentPanel.setBackground(Color.WHITE);
        selectedAgentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // 여백 설정

        // AgentButtonListener 초기화
        agentButtonListener = new AgentButtonListener();

        for (int i = 0; i < selectedAgentButtons.length; i++) {
            selectedAgentButtons[i] = new JButton();  // 변경된 부분
            selectedAgentButtons[i].setPreferredSize(new Dimension(100, 100));
            selectedAgentButtons[i].setOpaque(false);
            selectedAgentButtons[i].setVisible(false);
            selectedAgentButtons[i].setBorderPainted(false);
            selectedAgentButtons[i].setContentAreaFilled(false);
            selectedAgentPanel.add(selectedAgentButtons[i]);
        }

        // selectedAgentPanel의 크기를 조정
        Dimension selectedAgentPanelSize = new Dimension(700, 100);
        selectedAgentPanel.setPreferredSize(selectedAgentPanelSize);
        mainPanel.add(selectedAgentPanel);

        // 요원 정보 텍스트
        // 요원 이름
        infoLabel_AgName = new JLabel();
        infoLabel_AgName.setFont(new Font("맑은 고딕", Font.BOLD, 30));
        infoLabel_AgName.setAlignmentX(Component.CENTER_ALIGNMENT); // 가운데 정렬
        infoLabel_AgName.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // 여백 설정
        infoLabel_AgName.setText(" ");
        mainPanel.add(infoLabel_AgName);

        // 요원 포지션
        infoLabel_AgPosition = new JLabel();
        infoLabel_AgPosition.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        infoLabel_AgPosition.setAlignmentX(Component.CENTER_ALIGNMENT); // 가운데 정렬
        infoLabel_AgPosition.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // 여백 설정
        infoLabel_AgPosition.setText(" ");
        mainPanel.add(infoLabel_AgPosition);

        // 선택된 요원 조합에 따라 변경되는 텍스트 패널
        // selectedCombinationPanel
        selectedCombinationPanel = new JPanel();
        selectedCombinationPanel.setLayout(new FlowLayout());
        selectedCombinationPanel.setBackground(Color.WHITE);
        selectedCombinationPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // 여백 조절

        // selectedCombinationLabel
        selectedCombinationLabel = new JLabel();
        selectedCombinationLabel.setText(" ");
        selectedCombinationLabel.setForeground(Color.RED); // 텍스트 색을 빨간색으로 설정
        selectedCombinationLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        selectedCombinationPanel.add(selectedCombinationLabel);
        mainPanel.add(selectedCombinationPanel);

        // 요원 이미지를 선택하는 버튼 패널
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 150, 300, 150)); // 여백 설정

        // 요원 이미지 버튼들 (1~23)
        agentInfoMap = new HashMap<>();
        agentInfoMap.put(1, new String[]{"제트", "타격대"});
        agentInfoMap.put(2, new String[]{"레이즈", "타격대"});
        agentInfoMap.put(3, new String[]{"브리치", "척후대"});
        agentInfoMap.put(4, new String[]{"오멘", "전략가"});
        agentInfoMap.put(5, new String[]{"브림스톤", "전략가"});
        agentInfoMap.put(6, new String[]{"피닉스", "타격대"});
        agentInfoMap.put(7, new String[]{"세이지", "감시자"});
        agentInfoMap.put(8, new String[]{"소바", "척후대"});
        agentInfoMap.put(9, new String[]{"바이퍼", "전략가"});
        agentInfoMap.put(10, new String[]{"사이퍼", "감시자"});
        agentInfoMap.put(11, new String[]{"레이나", "타격대"});
        agentInfoMap.put(12, new String[]{"킬조이", "감시자"});
        agentInfoMap.put(13, new String[]{"스카이", "척후대"});
        agentInfoMap.put(14, new String[]{"요루", "타격대"});
        agentInfoMap.put(15, new String[]{"아스트라", "전략가"});
        agentInfoMap.put(16, new String[]{"케이/오", "척후대"});
        agentInfoMap.put(17, new String[]{"체임버", "감시자"});
        agentInfoMap.put(18, new String[]{"네온", "타격대"});
        agentInfoMap.put(19, new String[]{"페이드", "척후대"});
        agentInfoMap.put(20, new String[]{"하버", "전략가"});
        agentInfoMap.put(21, new String[]{"게코", "척후대"});
        agentInfoMap.put(22, new String[]{"데드록", "감시자"});
        agentInfoMap.put(23, new String[]{"아이소", "타격대"});

        // 요원 이미지 버튼들 (1~23)
        for (int i = 1; i <= 23; i++) {
            ImageIcon agentImageIcon = new ImageIcon(Menu.class.getResource("/images/agent" + i + ".png"));
            Image agentImage = agentImageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JButton agentButton = new CircularButton(new ImageIcon(agentImage));

            // 이미지 경로를 버튼의 액션 커맨드로 저장
            agentButton.setActionCommand("/images/agent" + i + ".png");

            agentButton.setBackground(Color.WHITE);

            agentButton.addActionListener(agentButtonListener);
            buttonPanel.add(agentButton);
        }

        mainPanel.add(buttonPanel);

        // mainPanel 추가
        add(mainPanel);
    }

    // 요원 이미지 버튼 클릭 리스너
    private class AgentButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton sourceButton = (JButton) e.getSource();

            // 이미 선택된 버튼을 클릭했을 경우
            for (int i = 0; i < selectedAgentButtons.length; i++) {
                if (selectedAgentButtons[i].isVisible() && selectedAgentButtons[i].getIcon() == sourceButton.getIcon()) {
                    // 이미 선택된 경우 제거
                    selectedAgentButtons[i].setVisible(false);

                    // 선택 요원 수 업데이트 + 텍스트 업데이트
                    updateInstructionLabel();
                    updateSelectedCombinationLabel();

                    infoLabel_AgName.setText(" ");
                    infoLabel_AgPosition.setText(" ");

                    // 버튼 투명도 초기화
                    if (sourceButton instanceof CircularButton) {
                        ((CircularButton) sourceButton).setAlpha(1.0f);
                    }

                    return;
                }
            }

            // 선택된 요원 수를 업데이트하고 빈 슬롯 찾기
            for (int i = 0; i < selectedAgentButtons.length; i++) {
                if (!selectedAgentButtons[i].isVisible()) {
                    // 선택한 요원 이미지를 selectedAgentButtons 배열에 추가
                    ImageIcon selectedAgentIcon = (ImageIcon) sourceButton.getIcon();
                    selectedAgentButtons[i].setIcon(selectedAgentIcon);
                    selectedAgentButtons[i].setVisible(true);

                    // 클릭된 요원 버튼 식별
                    String imagePath = e.getActionCommand();
                    String[] pathComponents = imagePath.split("/");
                    String imageName = pathComponents[pathComponents.length - 1];
                    int agentIndex = Integer.parseInt(imageName.substring(5, imageName.indexOf('.')));

                    // 요원 정보 설정
                    if (agentInfoMap.containsKey(agentIndex)) {
                        String[] agentInfo = agentInfoMap.get(agentIndex);
                        infoLabel_AgName.setText(agentInfo[0]);
                        infoLabel_AgPosition.setText(agentInfo[1]);
                    } else {
                        infoLabel_AgPosition.setText("해당 요원에 대한 데이터가 없습니다.");
                    }

                    // agentButton의 투명도 조절 (여기서는 50%로 설정)
                    if (sourceButton instanceof CircularButton) {
                        ((CircularButton) sourceButton).setAlpha(0.5f);
                    }

                    // 요원 선택 후 처리
                    // agentInfoMap에서 해당 인덱스의 정보 가져오기
                    if (agentInfoMap.containsKey(agentIndex)) {
                        String[] agentInfo = agentInfoMap.get(agentIndex);

                        // agentInfo 배열에서 필요한 정보 가져오기
                        String agentName = agentInfo[0];
                        String agentPosition = agentInfo[1];

                        // agentPositions 배열에 포지션 정보 삽입
                        agentPositions[i] = agentPosition;

                        // 콘솔 출력
                        System.out.println("Selected Agent Button " + (i + 1) + ": " + agentIndex);
                        System.out.println("Agent Name: " + agentName);
                        System.out.println("Agent Position: " + agentPosition);
                    } else {
                        System.out.println("Agent information not found for index: " + agentIndex);
                    }

                    // 선택 요원 수 업데이트 + 텍스트 업데이트
                    updateInstructionLabel();
                    updateSelectedCombinationLabel();

                    System.out.println();

                    break; // 빈 슬롯을 찾으면 루프 종료
                }
            }
        }

        // 선택된 요원 수를 표시하는 레이블 업데이트
        private void updateInstructionLabel() {
            int selectedCount = 0;
            for (JButton button : selectedAgentButtons) {
                if (button.isVisible()) {
                    selectedCount++;
                }
            }
            instructionLabel.setText("요원을 선택하세요. (" + selectedCount + "/5)");
        }

        // 요원 선택을 완료(5/5)하면 선택된 요원 조합에 따라 텍스트 업데이트
        private void updateSelectedCombinationLabel() {
            int selectedCount = 0;

            int taekukdaeCount = 0;
            int cheokhudaedaeCount = 0;
            int gamsijaCount = 0;
            int jeonraggaCount = 0;

            for (JButton button : selectedAgentButtons) {
                if (button.isVisible()) {
                    selectedCount++;
                }
            }

            if (selectedCount == 5) {
                // 각 포지션 개수 세기
                for (String position : agentPositions) {
                    if (position != null) {
                        switch (position) {
                            case "타격대":
                                taekukdaeCount++;
                                break;
                            case "척후대":
                                cheokhudaedaeCount++;
                                break;
                            case "감시자":
                                gamsijaCount++;
                                break;
                            case "전략가":
                                jeonraggaCount++;
                                break;
                        }
                    }
                }

                // 선택된 요원들의 포지션 정보 출력
                System.out.print("Selected Agent Positions: ");
                for (String position : agentPositions) {
                    if (position != null) {
                        System.out.print(position + ", ");
                    }
                }
                System.out.println();

                // 포지션 조합에 따라 텍스트 출력
                if (taekukdaeCount == 2 && cheokhudaedaeCount == 1 && gamsijaCount == 1 && jeonraggaCount == 1) {
                    selectedCombinationLabel.setText("승률이 높은 추천 조합입니다.");
                } else {
                    StringBuilder message = new StringBuilder(" ");

                    // 타격대가 2 초과일 때 안내 메시지 추가
                    if (taekukdaeCount > 2) {
                        message.append("타격대가 너무 많습니다. ");
                    }

                    // 척후대가 1 초과일 때 안내 메시지 추가
                    if (cheokhudaedaeCount > 1) {
                        message.append("척후대가 너무 많습니다. ");
                    }

                    // 감시자가 1 초과일 때 안내 메시지 추가
                    if (gamsijaCount > 1) {
                        message.append("감시자가 너무 많습니다. ");
                    }

                    // 전략가가 1 초과일 때 안내 메시지 추가
                    if (jeonraggaCount > 1) {
                        message.append("전략가가 너무 많습니다. ");
                    }

                    message.append("추천 요원 : ");

                    // 타격대가 2 미만일 때 안내 메시지 추가
                    if (taekukdaeCount < 2) {
                        message.append("타격대 ");
                    }

                    // 척후대가 1 미만일 때 안내 메시지 추가
                    if (cheokhudaedaeCount < 1) {
                        message.append("척후대 ");
                    }

                    // 감시자가 1 미만일 때 안내 메시지 추가
                    if (gamsijaCount < 1) {
                        message.append("감시자 ");
                    }

                    // 전략가가 1 미만일 때 안내 메시지 추가
                    if (jeonraggaCount < 1) {
                        message.append("전략가 ");
                    }

                    // 안내 메시지 설정
                    selectedCombinationLabel.setText(message.toString());
                }

            } else {
                selectedCombinationLabel.setText(" ");
            }
        }

    }

}
