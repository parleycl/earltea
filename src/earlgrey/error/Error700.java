package earlgrey.error;

import earlgrey.annotations.ErrorCode;

public class Error700 extends ErrorBase{
	@ErrorCode(description="The app don't have permissions to write in the disk.", code=701)
	public static int FILESYSTEM_WRITE_ERROR = 701;
	@ErrorCode(description="There was an error while writing the file. Verify the permisions.", code=701)
	public static int FILE_SAVE_ERROR = 702;
	@ErrorCode(description="Error when Ealrgrey read the file system.", code=703)
	public static int FILE_READ_ERROR = 703;
	@ErrorCode(description="The file defined is void.", code=704)
	public static int FILE_EMPTY_ERROR = 704;
	@ErrorCode(description="The file specify are damaged.", code=705)
	public static int FILE_DAMAGE_ERROR = 705;
}
