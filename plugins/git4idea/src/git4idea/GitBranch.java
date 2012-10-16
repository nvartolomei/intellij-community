/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package git4idea;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.repo.GitRepositoryFiles;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * This data class represents a Git branch
 */
public class GitBranch extends GitReference {
  @NonNls public static final String REFS_HEADS_PREFIX = "refs/heads/"; // Prefix for local branches ({@value})
  @NonNls public static final String REFS_REMOTES_PREFIX = "refs/remotes/"; // Prefix for remote branches ({@value})

  private final boolean myRemote;
  private static final Logger LOG = Logger.getInstance(GitBranch.class);
  private final String myHash;

  public GitBranch(@NotNull String name, @NotNull String hash, boolean remote) {
    super(name);
    myRemote = remote;
    myHash = new String(hash.trim());
  }
  
  @Deprecated
  public GitBranch(@NotNull String name, boolean remote) {
    this(name, "", remote);
  }

  /**
   * @return true if the branch is remote
   */
  public boolean isRemote() {
    return myRemote;
  }

  @NotNull
  public String getFullName() {
    return (myRemote ? REFS_REMOTES_PREFIX : REFS_HEADS_PREFIX) + myName;
  }

  /**
   * <p>
   *   Returns the "local" name of a remote branch.
   *   For example, for "origin/master" returns "master".
   * </p>
   * <p>
   *   Note that slashes are not permitted in remote names, so if we know that a branch is a remote branch,
   *   we know that local branch name is tha part after the slash.
   * </p>
   * @return "local" name of a remote branch, or just {@link #getName()} for local branches.
   */
  @NotNull
  public String getShortName() {
    return splitNameOfRemoteBranch(getName()).getSecond();
  }

  /**
   * Returns the remote and the "local" name of a remote branch.
   * Expects branch in format "origin/master", i.e. remote/branch
   */
  public static Pair<String, String> splitNameOfRemoteBranch(String branchName) {
    int firstSlash = branchName.indexOf('/');
    String remoteName = firstSlash > -1 ? branchName.substring(0, firstSlash) : branchName;
    String remoteBranchName = branchName.substring(firstSlash + 1);
    return Pair.create(remoteName, remoteBranchName);
  }

  /**
   * Checks if the branch exists in the repository.
   * @return true if the branch exists, false otherwise.
   * @deprecated use {@link git4idea.repo.GitRepository#getBranches()}
   */
  public boolean exists(VirtualFile root) {
    final VirtualFile remoteBranch = root.findFileByRelativePath(GitRepositoryFiles.GIT_REFS_REMOTES + "/" + myName);
    if (remoteBranch != null && remoteBranch.exists()) {
      return true;
    }
    final VirtualFile packedRefs = root.findFileByRelativePath(GitRepositoryFiles.GIT_PACKED_REFS);
    if (packedRefs != null && packedRefs.exists()) {
      final byte[] contents;
      try {
        contents = packedRefs.contentsToByteArray();
        return new String(contents).contains(myName);
      } catch (IOException e) {
        LOG.info("exists ", e);
        return false;
      }
    }
    return false;
  }

  /**
   * Returns the hash on which this branch is reference to.
   * May be empty, if this information wasn't supplied to the GitBranch constructor.
   */
  @NotNull
  public String getHash() {
    return myHash;
  }
}
