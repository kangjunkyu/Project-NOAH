import Header from "./../components/common/Header";
import styles from "./MyPage.module.css";
import { ReactComponent as History } from "./../assets/Icon/History.svg";
import { ReactComponent as Auto } from "./../assets/Icon/Auto.svg";
import { ReactComponent as Logout } from "./../assets/Icon/Logout.svg";
import { ReactComponent as Account } from "./../assets/Icon/Account.svg";

export default function MyPage() {
  return (
    <>
      <Header LeftIcon="Cancel" Title="마이페이지" />
      <div className={styles.infoContainer}>
        <div className={styles.labelLarge}>큐티핸섬준규</div>
        <div className={styles.labelMedium}>강준규</div>
        <div className={styles.labelSmall}>2024-03-01 가입</div>
      </div>
      <div className={styles.menuContainer}>
        <History className={styles.icon} />
        <div className={styles.labelLarge}>지난여행</div>
      </div>
      <div className={styles.menuContainer}>
        <Account className={styles.icon} />
        <div className={styles.labelLarge}>내 계좌</div>
      </div>
      <div className={styles.menuContainer}>
        <Auto className={styles.icon} />
        <div className={styles.labelLarge}>자동이체 설정</div>
      </div>
      <div className={styles.menuContainer}>
        <Logout className={styles.icon} />
        <div className={styles.labelLarge}>로그아웃</div>
      </div>
      <div className={styles.bottom}></div>
    </>
  );
}
