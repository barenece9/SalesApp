package apps.lnsel.com.ambujasales.helpers.NavigationDrawer;

/**
 * Created by apps2 on 4/20/2017.
 */
public class NavItem {

    private String title;
    private String subTitle;
    private int resIcon;

    public NavItem(String title, String subTitle, int resIcon) {
        super();
        this.title = title;
        this.subTitle = subTitle;
        this.resIcon = resIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

}
