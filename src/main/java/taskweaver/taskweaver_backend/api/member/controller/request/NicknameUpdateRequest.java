package taskweaver.taskweaver_backend.api.member.controller.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "닉네임 업데이트 요청")
public class NicknameUpdateRequest {

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Schema(description = "닉네임", example = "닉네임")
    private String nickname;

}
